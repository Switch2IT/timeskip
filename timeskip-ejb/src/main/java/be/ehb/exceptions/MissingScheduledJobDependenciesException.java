package be.ehb.exceptions;

/**
 * @author Patrick Van den Bussche
 * @since 2017
 */
public class MissingScheduledJobDependenciesException extends AbstractNotFoundException {

    public MissingScheduledJobDependenciesException(String message) {
        super(message);
    }

    @Override
    public int getErrorCode() {
        return ErrorCodes.getSchedulerDependenciesMissing();
    }
}