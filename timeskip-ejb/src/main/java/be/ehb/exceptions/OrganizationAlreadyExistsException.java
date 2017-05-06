package be.ehb.exceptions;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public class OrganizationAlreadyExistsException extends AbstractAlreadyExistsException {

    public OrganizationAlreadyExistsException(String message) {
        super(message);
    }

    @Override
    public int getErrorCode() {
        return ErrorCodes.ORGANIZATION_ALREADY_EXISTS;
    }
}