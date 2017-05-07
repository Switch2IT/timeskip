package be.ehb.exceptions;

/**
 * Created by Patrick Van den Bussche on 7/05/2017.
 */
public class SchedulerNotFoundException extends AbstractNotFoundException {

    public SchedulerNotFoundException(String message) {
        super(message);
    }

    public SchedulerNotFoundException(Throwable cause) {
        super(cause);
    }

    @Override
    public int getErrorCode() {
        return ErrorCodes.SCHEDULER_NOT_FOUND;
    }
}