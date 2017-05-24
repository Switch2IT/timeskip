package be.ehb.exceptions;

import javax.ws.rs.core.Response;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public class IdpException extends AbstractSystemException {

    public IdpException(String message) {
        super(message);
    }

    @Override
    public Response.Status getHttpCode() {
        return Response.Status.INTERNAL_SERVER_ERROR;
    }

    @Override
    public int getErrorCode() {
        return ErrorCodes.getIdpError();
    }
}