package be.ehb.exceptions;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public abstract class AbstractSystemException extends AbstractRestException {

    public AbstractSystemException() {
    }

    public AbstractSystemException(String message) {
        super(message);
    }

    public AbstractSystemException(Throwable cause) {
        super(cause);
    }

    public AbstractSystemException(String message, Throwable cause) {
        super(message, cause);
    }
}