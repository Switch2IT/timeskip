package be.ehb.exceptions;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public class ActivityAlreadyExistsException extends AbstractAlreadyExistsException {

    public ActivityAlreadyExistsException(String message) {
        super(message);
    }

    @Override
    public int getErrorCode() {
        return ErrorCodes.ACTIVITY_ALREADY_EXISTS;
    }
}