package be.ehb.exceptions;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public class PaygradeAlreadyExistsException extends AbstractAlreadyExistsException {

    public PaygradeAlreadyExistsException(String message) {
        super(message);
    }

    @Override
    public int getErrorCode() {
        return ErrorCodes.getPaygradeAlreadyExists();
    }
}