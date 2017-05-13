package be.ehb.exceptions;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public class RoleStillInUseException extends AbstractInvalidInputException {

    public RoleStillInUseException(String message) {
        super(message);
    }

    @Override
    public int getErrorCode() {
        return ErrorCodes.ROLE_STILL_IN_USE;
    }
}