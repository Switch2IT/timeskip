package be.ehb.exceptions;

/**
 * @author Patrick Van den Bussche
 * @since 2017
 */
public class SchedulerUnableToStartException extends AbstractNotFoundException {

    public SchedulerUnableToStartException(String message) {
        super(message);
    }

    public SchedulerUnableToStartException(Throwable cause) {
        super(cause);
    }

    @Override
    public int getErrorCode() {
        return ErrorCodes.SCHEDULER_NOT_FOUND;
    }
}