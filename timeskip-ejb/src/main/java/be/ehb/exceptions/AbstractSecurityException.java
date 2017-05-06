package be.ehb.exceptions;

import javax.ws.rs.core.Response;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public abstract class AbstractSecurityException extends AbstractRestException {

    AbstractSecurityException() {
        super();
    }

    AbstractSecurityException(String message) {
        super(message);
    }

    AbstractSecurityException(Throwable cause) {
        super(cause);
    }

    AbstractSecurityException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public Response.Status getHttpCode() {
        return Response.Status.FORBIDDEN;
    }

}