package be.ehb.exceptions;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public class WorklogNotFoundException extends AbstractNotFoundException {

    public WorklogNotFoundException(String message) {
        super(message);
    }

    public WorklogNotFoundException(Throwable cause) {
        super(cause);
    }

    @Override
    public int getErrorCode() {
        return ErrorCodes.WORKLOG_NOT_FOUND;
    }
}