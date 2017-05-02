package be.ehb.mail;

import javax.mail.internet.MimeMessage;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public interface IMailProvider {
    <K extends MimeMessage> void sendMail(K message);

    <K extends MimeMessage> K composeMessage(BaseMailBean mailContent);
}