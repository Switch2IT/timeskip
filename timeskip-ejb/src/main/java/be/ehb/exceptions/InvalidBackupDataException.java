package be.ehb.exceptions;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public class InvalidBackupDataException extends AbstractInvalidInputException {

    public InvalidBackupDataException(String message) {
        super(message);
    }

    @Override
    public int getErrorCode() {
        return ErrorCodes.getInvalidBackup();
    }
}