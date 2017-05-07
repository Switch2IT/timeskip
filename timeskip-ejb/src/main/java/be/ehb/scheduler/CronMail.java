package be.ehb.scheduler;


import be.ehb.factories.ExceptionFactory;
import org.quartz.*;
import org.quartz.Scheduler;
import org.quartz.impl.StdSchedulerFactory;


/**
 * Created by Patrick Van den Bussche on 7/05/2017.
 */
public class CronMail {

    private Scheduler sf;

    public void CronMailMothly(){
        try {
            sf = new StdSchedulerFactory().getScheduler();
        }
        catch (SchedulerException ex) {
            throw ExceptionFactory.schedulerNotFoundException("???");
        }
        JobDetail job = JobBuilder.newJob(SimpleJob.class).withIdentity("job1", "group1").build();
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
}
