package be.ehb.exceptions;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public class MailTemplateNotFoundException extends AbstractNotFoundException {

    public MailTemplateNotFoundException(String message) {
        super(message);
    }

    @Override
    public int getErrorCode() {
        return ErrorCodes.getMailTemplateNotFound();
    }
}