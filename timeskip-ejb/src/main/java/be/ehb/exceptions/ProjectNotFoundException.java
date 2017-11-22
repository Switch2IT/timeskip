package be.ehb.exceptions;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public class ProjectNotFoundException extends AbstractNotFoundException {

    public ProjectNotFoundException(String message) {
        super(message);
    }

    @Override
    public int getErrorCode() {
        return ErrorCodes.getProjectNotFound();
    }
}