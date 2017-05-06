package be.ehb.exceptions;

import javax.ws.rs.core.Response;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public abstract class AbstractNotFoundException extends AbstractRestException {

    AbstractNotFoundException(String message) {
        super(message);
    }

    AbstractNotFoundException(Throwable cause) {
        super(cause);
    }

    @Override
    public Response.Status getHttpCode() {
        return Response.Status.NOT_FOUND;
    }
}