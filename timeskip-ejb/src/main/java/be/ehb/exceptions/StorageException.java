package be.ehb.exceptions;

import javax.ws.rs.core.Response;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public class StorageException extends AbstractSystemException {

    public StorageException(String message) {
        super(message);
    }

    public StorageException(Throwable cause) {
        super(cause);
    }

    @Override
    public int getHttpCode() {
        return Response.Status.INTERNAL_SERVER_ERROR.getStatusCode();
    }

    @Override
    public int getErrorCode() {
        return ErrorCodes.SYSTEM_ERROR;
    }
}