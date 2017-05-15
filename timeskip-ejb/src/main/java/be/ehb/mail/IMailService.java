package be.ehb.mail;

import be.ehb.model.mail.ConfirmationReminderMailBean;
import be.ehb.model.mail.PrefillTimeSheetMailBean;

/**
 * @author Guillaume Vandecasteele / Patrick Van den Bussche
 * @since 2017
 */
public interface IMailService {

    void sendStartupMail();

    void sendConfirmationReminder(ConfirmationReminderMailBean reminder);

    void sendPrefillTimeSheet(PrefillTimeSheetMailBean prefill);
}
