package be.ehb.scheduler;


import be.ehb.factories.ExceptionFactory;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import javax.ejb.Singleton;
import javax.enterprise.context.ApplicationScoped;


/**
 * Created by Patrick Van den Bussche on 7/05/2017.
 */
@ApplicationScoped
@Singleton
public class ScheduleService {

    private org.quartz.Scheduler sf;

    public void ScheduleStart() {
        try {
            sf = new StdSchedulerFactory().getScheduler();
        }
        catch (SchedulerException ex) {
            throw ExceptionFactory.schedulerNotFoundException("???");
        }
        JobDetail job = JobBuilder.newJob(EmailReminderJob.class).withIdentity("job1", "group1").build();
        //TODO get date & time from config file, now every first day of month at 10:00 hrs
        //CronTrigger trigger = TriggerBuilder.newTrigger().forJob(job).withSchedule(CronScheduleBuilder.cronSchedule("0 0 10 1 * ? *")).build();
        //TODO for test reason changed to every 3 minutes
        CronTrigger trigger = TriggerBuilder.newTrigger().forJob(job).withSchedule(CronScheduleBuilder.cronSchedule("0 0/3 * * * ? *")).build();
        try {
            sf.addJob(job, true);
        }
        catch (SchedulerException ex) {
            throw ExceptionFactory.schedulerUnableToAddJobException("???");
        }
        try {
            sf.start();
        }
        catch (SchedulerException ex) {
            throw ExceptionFactory.schedulerUnableToStartException("???");
        }
        try {
            sf.scheduleJob(job, trigger);
        }
        catch (SchedulerException ex) {
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
