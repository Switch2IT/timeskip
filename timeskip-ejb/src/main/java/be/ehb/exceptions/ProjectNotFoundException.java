package be.ehb.exceptions;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public class ProjectNotFoundException extends AbstractNotFoundException {

    public ProjectNotFoundException(String message) {
        super(message);
    }

    public ProjectNotFoundException(Throwable cause) {
        super(cause);
    }

    @Override
    public int getErrorCode() {
        return ErrorCodes.PROJECT_NOT_FOUND;
    }
}