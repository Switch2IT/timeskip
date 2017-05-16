package be.ehb.scheduler;


import be.ehb.configuration.IAppConfig;
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
import javax.inject.Inject;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;


/**
 * @author Patrick Van den Bussche
 * @since 2017
 */
@ApplicationScoped
@Singleton
public class ScheduleService {

    private static final Logger log = LoggerFactory.getLogger(ScheduleService.class);
    private org.quartz.Scheduler sf;

    @Inject
    private IMailService mailService;
    @Inject
    private IStorageService iss;
    @Inject
    private IAppConfig iAppConfig;
    @Inject
    private IOrganizationFacade iOrganizationFacade;

    public void ScheduleStart() {
        try {
            sf = new StdSchedulerFactory().getScheduler();
        }
        catch (SchedulerException ex) {
            throw ExceptionFactory.schedulerNotFoundException("ScheduleService");
        }

        EmailReminderJobContext mailJob = new EmailReminderJobContext();
        mailJob.setIss(iss);
        mailJob.setMailService(mailService);
        PrefillTimeSheetsJobContext prefillJob = new PrefillTimeSheetsJobContext();
        prefillJob.setIss(iss);
        prefillJob.setMailService(mailService);
        prefillJob.setiof(iOrganizationFacade);


        JobDetail job1 = JobBuilder.newJob(EmailReminderJob.class).withIdentity("job1", "group1").build();
        JobDetail job2 = JobBuilder.newJob(PrefillTimeSheetsJob.class).withIdentity("job2", "group1").build();
        Integer dayOfMonthlyReminderEmail = iAppConfig.getDayOfMonthlyReminderEmail();
        Integer lastDayOfMonth = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth()).getDayOfMonth();
        log.info("lastDayOfMonth = " + lastDayOfMonth );
        if (dayOfMonthlyReminderEmail < 1 || dayOfMonthlyReminderEmail > lastDayOfMonth){
            //config DayOfMonthlyReminderEmail to 1, if faulty or not properly set
            log.warn("Config DayOfMonthlyReminderEmail reset by Scheduler to \"1\"");
            dayOfMonthlyReminderEmail = 1;
        }
        //seconds=0 minutes=0 hours=1 dayOfMonth=? month=every dayOfWeek=undefined year=every
        //TODO change for production
        // String cronTrigger1 = "0 0 1 " + dayOfMonthlyReminderEmail.toString() + " * ? *";
        String cronTrigger1 = "0 0/1 * * * ? *";
        log.info("cronTrigger EmailReminderJob = " + cronTrigger1 );
        CronTrigger trigger1 = TriggerBuilder.newTrigger().forJob(job1).withSchedule(CronScheduleBuilder.cronSchedule(cronTrigger1)).build();
        //seconds=0 minutes=0 hours=1 dayOfMonth=every month=every dayOfWeek=Monday year=every
        //TODO change for production
        //String cronTrigger2 = "0 0 1 ? * MON *";
        String cronTrigger2 = "0 0/1 * * * ? *";
        log.info("cronTrigger PrefillTimeSheetsJob = " + cronTrigger2 );
        CronTrigger trigger2 = TriggerBuilder.newTrigger().forJob(job2).withSchedule(CronScheduleBuilder.cronSchedule(cronTrigger2)).build();
        try {
            sf.start();
        } catch (SchedulerException ex) {
            log.error("Error starting scheduler : {}", ex);
            throw ExceptionFactory.schedulerUnableToStartException("ScheduleService");
        }
        try {
            sf.getContext().put(EmailReminderJob.EMAIL_REMINDER_CONTEXT, mailJob);
            sf.scheduleJob(job1, trigger1);
            sf.getContext().put(PrefillTimeSheetsJob.PREFILL_TIME_SHEETS_JOB_CONTEXT, prefillJob);
            sf.scheduleJob(job2, trigger2);
        }
        catch (SchedulerException ex) {
            ex.printStackTrace();
            throw ExceptionFactory.schedulerUnableToScheduleException("ScheduleService");
        }
    }

    public void ScheduleRestart() {
        //TODO when config changes
    }

    public void ScheduleStop() {
        //TODO when all services shutdown
    }
}
