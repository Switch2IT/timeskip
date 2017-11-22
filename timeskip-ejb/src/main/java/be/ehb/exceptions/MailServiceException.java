package be.ehb.exceptions;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public class MailServiceException extends AbstractSystemException {

    public MailServiceException(String message) {
        super(message);
    }

    @Override
    public int getErrorCode() {
        return ErrorCodes.getMailServiceError();
    }
}