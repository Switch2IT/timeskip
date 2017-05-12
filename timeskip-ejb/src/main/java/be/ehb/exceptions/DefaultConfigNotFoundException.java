package be.ehb.exceptions;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public class DefaultConfigNotFoundException extends AbstractSystemException {

    public DefaultConfigNotFoundException() {
    }

    public DefaultConfigNotFoundException(String message) {
        super(message);
    }

    public DefaultConfigNotFoundException(Throwable cause) {
        super(cause);
    }

    @Override
    public int getErrorCode() {
        return ErrorCodes.SYSTEM_ERROR;
    }
}