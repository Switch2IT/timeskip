package be.ehb.mail.provider;

import be.ehb.model.mail.BaseMailBean;

import javax.mail.internet.MimeMessage;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public interface IMailProvider {
    <K extends MimeMessage> void sendMail(K message);

    <K extends MimeMessage> K composeMessage(BaseMailBean mailContent);
}