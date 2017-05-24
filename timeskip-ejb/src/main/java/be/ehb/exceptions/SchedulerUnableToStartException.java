package be.ehb.exceptions;

/**
 * @author Patrick Van den Bussche/Guillaume Vandecasteele
 * @since 2017
 */
public class SchedulerUnableToStartException extends AbstractSystemException {

    @Override
    public int getErrorCode() {
        return ErrorCodes.getSchedulerUnableToStart();
    }
}