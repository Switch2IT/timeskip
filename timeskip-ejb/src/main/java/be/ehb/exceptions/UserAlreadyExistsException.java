package be.ehb.exceptions;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public class UserAlreadyExistsException extends AbstractAlreadyExistsException {

    public UserAlreadyExistsException(String message) {
        super(message);
    }

    @Override
    public int getErrorCode() {
        return ErrorCodes.getUserAlreadyExists();
    }
}