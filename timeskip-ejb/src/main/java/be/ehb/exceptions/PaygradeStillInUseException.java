package be.ehb.exceptions;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public class PaygradeStillInUseException extends AbstractInvalidInputException {

    public PaygradeStillInUseException(String message) {
        super(message);
    }

    @Override
    public int getErrorCode() {
        return ErrorCodes.PAYGRADE_STILL_IN_USE;
    }
}