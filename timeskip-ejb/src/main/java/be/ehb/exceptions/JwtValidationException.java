package be.ehb.exceptions;

import javax.ws.rs.core.Response;

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
        return Response.Status.FORBIDDEN.getStatusCode();
    }

    @Override
    public int getErrorCode() {
        return ErrorCodes.JWT_VALIDATION_ERROR;
    }
}