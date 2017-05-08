package be.ehb.exceptions;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public class MailTemplateNotFoundException extends AbstractNotFoundException {

    public MailTemplateNotFoundException(String message) {
        super(message);
    }

    public MailTemplateNotFoundException(Throwable cause) {
        super(cause);
    }

    @Override
    public int getErrorCode() {
        return ErrorCodes.MAIL_TEMPLATE_NOT_FOUND;
    }
}