package be.ehb.exceptions;

abstract class AbstractUserException extends AbstractRestException {


    AbstractUserException() {
    }

    AbstractUserException(String message) {
        super(message);
    }

    AbstractUserException(Throwable cause) {
        super(cause);
    }

    AbstractUserException(String message, Throwable cause) {
        super(message, cause);
    }

}
