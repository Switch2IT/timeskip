package be.ehb.exceptions;

import javax.ws.rs.core.Response;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public abstract class AbstractInvalidInputException extends AbstractRestException {

    AbstractInvalidInputException() {
        super();
    }

    AbstractInvalidInputException(String message) {
        super(message);
    }

    AbstractInvalidInputException(Throwable cause) {
        super(cause);
    }

    AbstractInvalidInputException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public int getHttpCode() {
        return Response.Status.BAD_REQUEST.getStatusCode();
    }
}