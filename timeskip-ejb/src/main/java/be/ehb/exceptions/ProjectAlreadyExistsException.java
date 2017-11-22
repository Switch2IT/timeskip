package be.ehb.exceptions;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public class ProjectAlreadyExistsException extends AbstractAlreadyExistsException {

    public ProjectAlreadyExistsException(String message) {
        super(message);
    }

    @Override
    public int getErrorCode() {
        return ErrorCodes.getProjectAlreadyExists();
    }
}