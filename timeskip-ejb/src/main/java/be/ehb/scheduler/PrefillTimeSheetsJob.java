package be.ehb.scheduler;

import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Patrick Van den Bussche
 * @since 2017
 */
public class PrefillTimeSheetsJob implements Job {

    static final String PREFILL_TIME_SHEETS_JOB_CONTEXT = "prefillTimeSheetsJobContext";
    private static final Logger log = LoggerFactory.getLogger(PrefillTimeSheetsJob.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        SchedulerContext schedulerContext;
        try {
            schedulerContext = context.getScheduler().getContext();
            final PrefillTimeSheetsJobContext validationJob = (PrefillTimeSheetsJobContext) schedulerContext.get(PREFILL_TIME_SHEETS_JOB_CONTEXT );
            if (validationJob != null) {
                validationJob.execute();
            }
        } catch (SchedulerException ex) {
            log.error("Error scheduling: {}", ex);
        }
    }
}
