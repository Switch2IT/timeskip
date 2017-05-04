package be.ehb.exceptions;

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
}