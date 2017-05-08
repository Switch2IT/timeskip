package be.ehb.exceptions;

import javax.ws.rs.core.Response;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public class NoUserContextException extends AbstractUserException {

    public NoUserContextException() {
    }

    public NoUserContextException(String message) {
        super(message);
    }

    public NoUserContextException(Throwable cause) {
        super(cause);
    }

    @Override
    public Response.Status getHttpCode() {
        return Response.Status.UNAUTHORIZED;
    }

    @Override
    public int getErrorCode() {
        return ErrorCodes.NO_USER_CONTEXT_FOUND;
    }
}