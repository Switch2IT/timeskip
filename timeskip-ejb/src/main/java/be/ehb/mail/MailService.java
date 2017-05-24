package be.ehb.mail;

import be.ehb.configuration.IAppConfig;
import be.ehb.entities.mail.MailTemplateBean;
import be.ehb.factories.ExceptionFactory;
import be.ehb.mail.provider.IMailProvider;
import be.ehb.model.mail.BaseMailBean;
import be.ehb.model.mail.ConfirmationReminderMailBean;
import be.ehb.model.mail.PrefillTimeSheetMailBean;
import be.ehb.storage.IStorageService;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.StrSubstitutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * @author Guillaume Vandecasteele / Patrick Van den Bussche
 * @since 2017
 */
@ApplicationScoped
@Default
public class MailService implements IMailService {

    private static final Logger log = LoggerFactory.getLogger(MailService.class);

    private static final String KEY_START = "{";
    private static final String KEY_END = "}";

    @Inject
    private IStorageService storage;
    @Inject
    private IAppConfig config;
    @Inject
    private IMailProvider mailProvider;

    @Override
    public void sendStartupMail() {
        BaseMailBean startupMail = new BaseMailBean();
        String startupRecipient = config.getNoticationStartupMailTo();
        if (StringUtils.isNotEmpty(startupRecipient)) {
            startupMail.setTo(config.getNoticationStartupMailTo());
            completeAndSendTemplate(MailTopic.STARTUP, startupMail);
        }
    }

    @Override
    public void sendConfirmationReminder(ConfirmationReminderMailBean reminder) {
        if (StringUtils.isEmpty(reminder.getUserName())) {
            throw ExceptionFactory.mailServiceException("\"userName\" must be provided in confirmation reminder");
        }
        if (StringUtils.isEmpty(reminder.getRequiredWorklogConfirmations())) {
            throw ExceptionFactory.mailServiceException("\"requiredWorklogConfirmations\" must be provided in confirmation reminder");
        }
        completeAndSendTemplate(MailTopic.CONFIRMATION_REMINDER, reminder);
    }

    @Override
    public void sendPrefillTimeSheet(PrefillTimeSheetMailBean prefill) {
        if (StringUtils.isEmpty(prefill.getUserName())) {
            throw ExceptionFactory.mailServiceException("\"userName\" must be provided in prefill time sheet ");
        }
        if (StringUtils.isEmpty(prefill.getPrefillWorklog())) {
            throw ExceptionFactory.mailServiceException("\"prefillWorklog\" must be provided in prefill time sheet");
        }
        completeAndSendTemplate(MailTopic.PREFILL_TIME_SHEET, prefill);
    }

    private void completeAndSendTemplate(MailTopic topic, BaseMailBean bean) {
        try {
            //Get the mail template
            MailTemplateBean template = storage.getMailTemplate(topic);
            //Prepare map with values from provided mail bean
            Map<String, String> keymap = BeanUtilsBean.getInstance().describe(bean);
            //Pass string substitutor along
            prepAndSendMail(new StrSubstitutor(keymap, KEY_START, KEY_END), template, bean.getTo());
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ex) {
            throw ExceptionFactory.mailServiceException(ex.getMessage());
        }
    }

    private void prepAndSendMail(StrSubstitutor sub, MailTemplateBean template, String to) {
        BaseMailBean mailBean = new BaseMailBean();
        mailBean.setSubject(sub.replace(template.getSubject()));
        mailBean.setContent(sub.replace(template.getContent()));
        mailBean.setTo(to);
        log.debug("Sending mail: {}", mailBean);
        mailProvider.sendMail(mailProvider.composeMessage(mailBean));
    }
}