package be.ehb.exceptions;

public abstract class AbstractUserException extends AbstractRestException {


    public AbstractUserException() {
    }

    public AbstractUserException(String message) {
        super(message);
    }

    public AbstractUserException(Throwable cause) {
        super(cause);
    }

    public AbstractUserException(String message, Throwable cause) {
        super(message, cause);
    }

}
