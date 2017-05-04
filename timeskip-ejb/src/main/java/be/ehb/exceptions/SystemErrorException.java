package be.ehb.exceptions;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public class SystemErrorException extends AbstractSystemException {

    public SystemErrorException(String message) {
        super(message);
    }

    public SystemErrorException(Throwable cause) {
        super(cause);
    }

    @Override
    public int getHttpCode() {
        return ErrorCodes.HTTP_STATUS_CODE_SYSTEM_ERROR;
    }

    @Override
    public int getErrorCode() {
        return ErrorCodes.SYSTEM_ERROR;
    }
}