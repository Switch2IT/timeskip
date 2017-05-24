package be.ehb.exceptions;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public class UnauthorizedException extends AbstractSecurityException {

    public UnauthorizedException() {
        super();
    }

    public UnauthorizedException(String message) {
        super(message);
    }

    @Override
    public int getErrorCode() {
        return ErrorCodes.getUnauthorizedForOrganization();
    }
}