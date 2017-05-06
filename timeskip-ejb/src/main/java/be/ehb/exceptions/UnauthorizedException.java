package be.ehb.exceptions;

import javax.ws.rs.core.Response;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public class UnauthorizedException extends AbstractUserException {

    public UnauthorizedException() {
    }

    public UnauthorizedException(String message) {
        super(message);
    }

    public UnauthorizedException(Throwable cause) {
        super(cause);
    }

    public UnauthorizedException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public int getHttpCode() {
        return Response.Status.FORBIDDEN.getStatusCode();
    }

    @Override
    public int getErrorCode() {
        return ErrorCodes.UNAUTHORIZED_FOR_ORGANIZATION;
    }
}