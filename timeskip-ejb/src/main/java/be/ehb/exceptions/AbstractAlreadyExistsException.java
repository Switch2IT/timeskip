package be.ehb.exceptions;

import javax.ws.rs.core.Response;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */

abstract class AbstractAlreadyExistsException extends AbstractRestException {

    AbstractAlreadyExistsException() {
        super();
    }

    AbstractAlreadyExistsException(String message) {
        super(message);
    }

    AbstractAlreadyExistsException(Throwable cause) {
        super(cause);
    }

    AbstractAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public Response.Status getHttpCode() {
        return Response.Status.CONFLICT;
    }
}