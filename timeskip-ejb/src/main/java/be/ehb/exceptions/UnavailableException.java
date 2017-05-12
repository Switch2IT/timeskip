package be.ehb.exceptions;

import javax.ws.rs.core.Response;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public class UnavailableException extends AbstractSystemException {

    public UnavailableException() {
        super();
    }

    @Override
    public Response.Status getHttpCode() {
        return Response.Status.SERVICE_UNAVAILABLE;
    }

    @Override
    public int getErrorCode() {
        return ErrorCodes.FUNCTIONALITY_UNAVAILABLE;
    }
}