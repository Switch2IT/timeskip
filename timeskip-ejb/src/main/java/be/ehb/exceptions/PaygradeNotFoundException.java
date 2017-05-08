package be.ehb.exceptions;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public class PaygradeNotFoundException extends AbstractNotFoundException {

    public PaygradeNotFoundException(String message) {
        super(message);
    }

    public PaygradeNotFoundException(Throwable cause) {
        super(cause);
    }

    @Override
    public int getErrorCode() {
        return ErrorCodes.PAYGRADE_NOT_FOUND;
    }
}