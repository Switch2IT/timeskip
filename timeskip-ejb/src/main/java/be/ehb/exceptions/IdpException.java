package be.ehb.exceptions;

import javax.ws.rs.core.Response;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public class IdpException extends AbstractSystemException {

    public IdpException(Throwable cause) {
        super(cause);
    }

    public IdpException(String message) {
        super(message);
    }

    public IdpException() {
    }

    @Override
    public int getHttpCode() {
        return Response.Status.INTERNAL_SERVER_ERROR.getStatusCode();
    }

    @Override
    public int getErrorCode() {
        return ErrorCodes.IDP_ERROR;
    }
}