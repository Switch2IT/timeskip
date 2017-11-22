package be.ehb.exceptions;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public class OrganizationNotFoundException extends AbstractNotFoundException {

    public OrganizationNotFoundException(String message) {
        super(message);
    }

    @Override
    public int getErrorCode() {
        return ErrorCodes.getOrganizationNotFound();
    }
}