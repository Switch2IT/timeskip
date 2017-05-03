package be.ehb.exceptions;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public class UserNotFoundException extends AbstractUserException {

    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException(Throwable cause) {
        super(cause);
    }

    @Override
    public int getHttpCode() {
        return ErrorCodes.HTTP_STATUS_CODE_NOT_FOUND;
    }

    @Override
    public int getErrorCode() {
        return ErrorCodes.USER_NOT_FOUND;
    }
}