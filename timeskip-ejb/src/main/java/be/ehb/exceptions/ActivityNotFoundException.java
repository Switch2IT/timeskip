package be.ehb.exceptions;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public class ActivityNotFoundException extends AbstractNotFoundException {

    public ActivityNotFoundException(String message) {
        super(message);
    }

    @Override
    public int getErrorCode() {
        return ErrorCodes.getActivityNotFound();
    }
}