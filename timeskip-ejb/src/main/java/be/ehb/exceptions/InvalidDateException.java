package be.ehb.exceptions;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public class InvalidDateException extends AbstractInvalidInputException {

    public InvalidDateException(String message) {
        super(message);
    }

    @Override
    public int getErrorCode() {
        return ErrorCodes.INVALID_DATE;
    }
}