package be.ehb.exceptions;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public class UserNotFoundException extends AbstractNotFoundException {

    public UserNotFoundException(String message) {
        super(message);
    }

    @Override
    public int getErrorCode() {
        return ErrorCodes.getUserNotFound();
    }
}