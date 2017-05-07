package be.ehb.exceptions;

/**
 * Created by Patrick Van den Bussche on 7/05/2017.
 */
public class SchedulerUnableToAddJobException extends AbstractNotFoundException {

    public SchedulerUnableToAddJobException(String message) {
        super(message);
    }

    public SchedulerUnableToAddJobException(Throwable cause) {
        super(cause);
    }

    @Override
    public int getErrorCode() {
        return ErrorCodes.SCHEDULER_UNABLE_TO_ADD_JOB;
    }
}