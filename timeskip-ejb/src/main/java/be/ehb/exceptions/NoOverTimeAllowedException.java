package be.ehb.exceptions;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public class NoOverTimeAllowedException extends AbstractInvalidInputException {

    public NoOverTimeAllowedException(String message) {
        super(message);
    }

    @Override
    public int getErrorCode() {
        return ErrorCodes.getProjecOvertimeExceeded();
    }
}