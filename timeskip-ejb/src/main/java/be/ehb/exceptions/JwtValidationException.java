package be.ehb.exceptions;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public class JwtValidationException extends AbstractSecurityException {

    public JwtValidationException(String message) {
        super(message);
    }

    @Override
    public int getErrorCode() {
        return ErrorCodes.getJwtValidationError();
    }
}