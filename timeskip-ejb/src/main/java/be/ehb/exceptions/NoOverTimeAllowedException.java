package be.ehb.exceptions;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public class NoOverTimeAllowedException extends AbstractInvalidInputException {

    public NoOverTimeAllowedException(String message) {
        super(message);
    }

    public NoOverTimeAllowedException(Throwable cause) {
        super(cause);
    }

    @Override
    public int getErrorCode() {
        return ErrorCodes.PROJEC_OVERTIME_EXCEEDED;
    }
}