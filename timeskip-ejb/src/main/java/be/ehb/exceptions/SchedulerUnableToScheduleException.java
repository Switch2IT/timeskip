package be.ehb.exceptions;

/**
 * Created by Patrick Van den Bussche on 7/05/2017.
 */
public class SchedulerUnableToScheduleException extends AbstractNotFoundException {

    public SchedulerUnableToScheduleException(String message) {
        super(message);
    }

    public SchedulerUnableToScheduleException(Throwable cause) {
        super(cause);
    }

    @Override
    public int getErrorCode() {
        return ErrorCodes.SCHEDULER_NOT_FOUND;
    }
}