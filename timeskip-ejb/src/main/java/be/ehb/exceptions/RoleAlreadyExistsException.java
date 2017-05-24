package be.ehb.exceptions;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public class RoleAlreadyExistsException extends AbstractAlreadyExistsException {

    public RoleAlreadyExistsException(String message) {
        super(message);
    }

    @Override
    public int getErrorCode() {
        return ErrorCodes.getRoleAlreadyExists();
    }
}