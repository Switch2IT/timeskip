package be.ehb.exceptions;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public class PaygradeAlreadyExistsException extends AbstractAlreadyExistsException {

    public PaygradeAlreadyExistsException(String message) {
        super(message);
    }

    public PaygradeAlreadyExistsException(Throwable cause) {
        super(cause);
    }

    @Override
    public int getErrorCode() {
        return ErrorCodes.PAYGRADE_ALREADY_EXISTS;
    }
}