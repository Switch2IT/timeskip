package be.ehb.exceptions;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public class JwtValidationException extends AbstractSecurityException {

    public JwtValidationException(String message) {
        super(message);
    }

    public JwtValidationException(Throwable cause) {
        super(cause);
    }

    @Override
    public int getErrorCode() {
        return ErrorCodes.JWT_VALIDATION_ERROR;
    }
}