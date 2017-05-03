package be.ehb.exceptions;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public class JwtValidationException extends AbstractUserException {

    public JwtValidationException(String message) {
        super(message);
    }

    public JwtValidationException(Throwable cause) {
        super(cause);
    }

    @Override
    public int getHttpCode() {
        return ErrorCodes.HTTP_STATUS_CODE_FORBIDDEN;
    }

    @Override
    public int getErrorCode() {
        return ErrorCodes.JWT_VALIDATION_ERROR;
    }
}