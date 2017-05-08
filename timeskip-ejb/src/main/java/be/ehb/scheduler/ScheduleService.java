package be.ehb.scheduler;


import be.ehb.factories.ExceptionFactory;
import be.ehb.mail.IMailService;
import be.ehb.storage.IStorageService;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import javax.ejb.Singleton;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 * @author Patrick Van den Bussche
 * @since 2017
 */
@ApplicationScoped
@Singleton
public class ScheduleService {

    private org.quartz.Scheduler sf;

    @Inject
    private IMailService mailService;
    @Inject
    private IStorageService iss;

    public void ScheduleStart() {
        try {
            sf = new StdSchedulerFactory().getScheduler();
        }
        catch (SchedulerException ex) {
            throw ExceptionFactory.schedulerNotFoundException("???");
        }

        EmailReminderJobContext mailJob = new EmailReminderJobContext();
        mailJob.setIss(iss);
        mailJob.setMailService(mailService);


        JobDetail job = JobBuilder.newJob(EmailReminderJob.class).withIdentity("job1", "group1").build();
        //TODO get date & time from config file, now every first day of month at 10:00 hrs
        //TODO get if the config toggle lastDayOfMonth is enabled and use following cron syntax: "0 0 0 3 L *"
        //CronTrigger trigger = TriggerBuilder.newTrigger().forJob(job).withSchedule(CronScheduleBuilder.cronSchedule("0 0 10 1 * ? *")).build();
        //TODO for test reason changed to every 3 minutes
        CronTrigger trigger = TriggerBuilder.newTrigger().forJob(job).withSchedule(CronScheduleBuilder.cronSchedule("0 0/3 * * * ? *")).build();
        try {
            sf.start();
        } catch (SchedulerException ex) {
            throw ExceptionFactory.schedulerUnableToStartException("???");
        }
        try {
            sf.getContext().put(EmailReminderJob.EMAIL_REMINDER_CONTEXT, mailJob);
            sf.scheduleJob(job, trigger);
        }
        catch (SchedulerException ex) {
            ex.printStackTrace();
            throw ExceptionFactory.schedulerUnableToScheduleException("???");
        }
    }

    public void ScheduleRestart() {
        //TODO when config changes
    }

    public void ScheduleStop() {
        //TODO when all services shutdown
    }
}
