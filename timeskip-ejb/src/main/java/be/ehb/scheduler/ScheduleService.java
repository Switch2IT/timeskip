package be.ehb.scheduler;


import be.ehb.configuration.IAppConfig;
import be.ehb.exceptions.SchedulerNotFoundException;
import be.ehb.exceptions.SchedulerUnableToStartException;
import be.ehb.facades.IOrganizationFacade;
import be.ehb.factories.ExceptionFactory;
import be.ehb.mail.IMailService;
import be.ehb.storage.IStorageService;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Singleton;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;


/**
 * @author Patrick Van den Bussche
 * @since 2017
 */
@ApplicationScoped
@Singleton
@Default
public class ScheduleService implements IScheduleService {

    private static final Logger log = LoggerFactory.getLogger(ScheduleService.class);

    private static final String REMINDER_JOB_NAME = "reminderEmailJob";
    private static final String PREFILL_JOB_NAME = "prefillJob";
    private static final String JOB_GROUP = "recurringTasks";
    private static final String REMINDER_CRON_PREFIX = "0 0 1 ";
    private static final String REMINDER_CRON_SUFFIX = " * ? *";

    //seconds=0 minutes=0 hours=1 dayOfMonth=every month=every dayOfWeek=Monday year=every
    private static final String PREFILL_CRON = "0 0 1 ? * MON *";

    private Scheduler sf;

    @Inject
    private IMailService mailService;
    @Inject
    private IStorageService storage;
    @Inject
    private IAppConfig config;
    @Inject
    private IOrganizationFacade orgFacade;

    @Override
    public void startAll() {
        scheduleEmailReminderJob();
        schedulePrefillJob();
    }

    @Override
    public void restartAll() {
        restartEmailReminderJob();
        restartPrefillJob();
    }

    @Override
    public void stopAll() {
        stopEmailReminderJob();
        stopPrefillJob();
    }

    @Override
    public void scheduleEmailReminderJob() {
        try {
            JobDetail reminderJobDetail = JobBuilder.newJob(EmailReminderJob.class)
                    .withIdentity(REMINDER_JOB_NAME, JOB_GROUP)
                    .build();

            Integer dayOfMonthlyReminderEmail = config.getDayOfMonthlyReminderEmail();
            Integer lastDayOfMonth = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth()).getDayOfMonth();

            if (dayOfMonthlyReminderEmail < 1 || dayOfMonthlyReminderEmail > lastDayOfMonth) {
                //config DayOfMonthlyReminderEmail to 1, if faulty or not properly set
                log.warn("Config DayOfMonthlyReminderEmail reset by Scheduler to \"1\"");
                dayOfMonthlyReminderEmail = 1;
            }
            log.info("lastDayOfMonth: {}", lastDayOfMonth);

            //seconds=0 minutes=0 hours=1 dayOfMonth=set by config month=every dayOfWeek=undefined year=every
            String cronTrigger = REMINDER_CRON_PREFIX + dayOfMonthlyReminderEmail.toString() + REMINDER_CRON_SUFFIX;
            log.info("cronTrigger EmailReminderJob: {}", cronTrigger);

            CronTrigger trigger = TriggerBuilder.newTrigger().forJob(reminderJobDetail).withSchedule(CronScheduleBuilder.cronSchedule(cronTrigger)).build();

            getScheduler().scheduleJob(reminderJobDetail, trigger);
        } catch (SchedulerException ex) {
            log.error("Unable to add reminder email job to scheduler: {}", ex);
        } catch (SchedulerUnableToStartException | SchedulerNotFoundException ex) {
            log.error("Scheduler unavailable");
        }
    }

    @Override
    public void stopEmailReminderJob() {
        try {
            getScheduler().deleteJob(JobKey.jobKey(REMINDER_JOB_NAME, JOB_GROUP));
        } catch (SchedulerException ex) {
            log.error("Unable to stop e-mail reminder job: {}", ex);
        }
    }

    @Override
    public void restartEmailReminderJob() {
        stopEmailReminderJob();
        scheduleEmailReminderJob();
    }

    @Override
    public void schedulePrefillJob() {
        try {
            JobDetail prefillJobDetail = JobBuilder.newJob(PrefillTimeSheetsJob.class)
                    .withIdentity(PREFILL_JOB_NAME, JOB_GROUP)
                    .build();
            log.info("cronTrigger PrefillTimeSheetsJob: {}", PREFILL_CRON);
            CronTrigger trigger = TriggerBuilder.newTrigger().forJob(prefillJobDetail).withSchedule(CronScheduleBuilder.cronSchedule(PREFILL_CRON)).build();

            getScheduler().scheduleJob(prefillJobDetail, trigger);
        } catch (SchedulerException ex) {
            log.error("Unable to add prefill job to scheduler: {}", ex);
        } catch (SchedulerUnableToStartException | SchedulerNotFoundException ex) {
            log.error("Scheduler unavailable");
        }
    }

    @Override
    public void stopPrefillJob() {
        try {
            getScheduler().deleteJob(JobKey.jobKey(PREFILL_JOB_NAME, JOB_GROUP));
        } catch (SchedulerException ex) {
            log.error("Unable to stop timesheets prefill job: {}", ex);
        }
    }

    @Override
    public void restartPrefillJob() {
        stopPrefillJob();
        schedulePrefillJob();
    }

    private Scheduler getScheduler() throws SchedulerNotFoundException, SchedulerUnableToStartException {
        try {
            if (sf == null) {
                sf = new StdSchedulerFactory().getScheduler();
            }
            if (!sf.getContext().containsKey(EmailReminderJob.STORAGE_SERVICE_CONTEXT_KEY) || sf.getContext().get(EmailReminderJob.STORAGE_SERVICE_CONTEXT_KEY) == null) {
                sf.getContext().put(EmailReminderJob.STORAGE_SERVICE_CONTEXT_KEY, storage);
            }
            if (!sf.getContext().containsKey(EmailReminderJob.MAIL_SERVICE_CONTEXT_KEY) || sf.getContext().get(EmailReminderJob.MAIL_SERVICE_CONTEXT_KEY) == null) {
                sf.getContext().put(EmailReminderJob.MAIL_SERVICE_CONTEXT_KEY, mailService);
            }
            if (!sf.getContext().containsKey(PrefillTimeSheetsJob.ORGANIZATION_FACADE_CONTEXT_KEY) || sf.getContext().get(PrefillTimeSheetsJob.ORGANIZATION_FACADE_CONTEXT_KEY) == null) {
                sf.getContext().put(PrefillTimeSheetsJob.ORGANIZATION_FACADE_CONTEXT_KEY, orgFacade);
            }
            if (!sf.isStarted()) {
                try {
                    sf.start();
                } catch (SchedulerException ex) {
                    log.error("Unable to start scheduler: {}", ex);
                    throw ExceptionFactory.schedulerUnableToStartException();
                }
            }
            return sf;
        } catch (SchedulerException ex) {
            log.error("Unable to instantiate scheduler: {}", ex);
            throw ExceptionFactory.schedulerNotFoundException();
        }
    }
}
