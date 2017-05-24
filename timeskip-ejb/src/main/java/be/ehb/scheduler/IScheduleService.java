package be.ehb.scheduler;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public interface IScheduleService {

    void startAll();

    void restartAll();

    void stopAll();

    void scheduleEmailReminderJob();

    void stopEmailReminderJob();

    void restartEmailReminderJob();

    void schedulePrefillJob();

    void stopPrefillJob();

    void restartPrefillJob();
}
