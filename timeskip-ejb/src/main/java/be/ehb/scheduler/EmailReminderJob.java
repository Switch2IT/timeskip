package be.ehb.scheduler;

import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Patrick Van den Bussche on 7/05/2017.
 */

public class EmailReminderJob implements Job {

    static final String EMAIL_REMINDER_CONTEXT = "emailReminderContext";
    private static final Logger log = LoggerFactory.getLogger(EmailReminderJob.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        SchedulerContext schedulerContext;
        try {
            schedulerContext = context.getScheduler().getContext();
            final EmailReminderJobContext validationJob = (EmailReminderJobContext) schedulerContext.get(EMAIL_REMINDER_CONTEXT);
            if (validationJob != null) {
                validationJob.execute();
            }
        } catch (SchedulerException ex) {
            log.error("Error scheduling: {}", ex);
        }
    }
}
