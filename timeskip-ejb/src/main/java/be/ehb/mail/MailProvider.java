package be.ehb.mail;

import be.ehb.configuration.IAppConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
@ApplicationScoped
@Default
public class MailProvider implements IMailProvider {

    private final static Logger log = LoggerFactory.getLogger(MailProvider.class.getName());

    @Resource(mappedName = "java:jboss/mail/Default")
    private Session mailSession;
    @Inject
    private IAppConfig config;

    @Override
    public <K extends MimeMessage> void sendMail(K m) {
        try {
            if (m != null) {
                Transport.send(m);
                log.debug("Mail sent: {}", m);
            }
        } catch (MessagingException e) {
            log.error("Error sending email:{}", e.getMessage());
        }
    }

    @Override
    public <K extends MimeMessage> K composeMessage(BaseMailBean mailContent) {
        String subject = mailContent.getSubject();
        String content = mailContent.getContent();
        String toAddress = mailContent.getTo();
        try {
            MimeMessage m = new MimeMessage(mailSession);
            Address from = new InternetAddress(config.getNotificationMailFrom());
            Address[] to = new InternetAddress[]{new InternetAddress(toAddress)};
            m.setFrom(from);
            m.setRecipients(Message.RecipientType.TO, to);
            m.setSubject(subject);
            m.setSentDate(new java.util.Date());
            m.setContent(content, "text/html; charset=utf-8");
            return (K) m;
        } catch (MessagingException e) {
            log.error("Error sending email:{}", e.getMessage());
            return null;
        }
    }
}