package be.ehb.exceptions;

import javax.ws.rs.core.Response;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
abstract class AbstractSystemException extends AbstractRestException {

    AbstractSystemException() {
    }

    AbstractSystemException(String message) {
        super(message);
    }

    AbstractSystemException(Throwable cause) {
        super(cause);
    }

    AbstractSystemException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public Response.Status getHttpCode() {
        return Response.Status.INTERNAL_SERVER_ERROR;
    }
}