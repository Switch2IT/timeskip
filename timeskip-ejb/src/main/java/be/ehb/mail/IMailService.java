package be.ehb.mail;

import be.ehb.model.mail.ConfirmationReminderMailBean;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public interface IMailService {

    void sendStartupMail();

    void sendConfirmationReminder(ConfirmationReminderMailBean reminder);
}
