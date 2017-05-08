package be.ehb.facades;

import be.ehb.configuration.IAppConfig;
import be.ehb.entities.config.ConfigBean;
import be.ehb.entities.mail.MailTemplateBean;
import be.ehb.factories.ResponseFactory;
import be.ehb.mail.MailTopic;
import be.ehb.model.requests.UpdateDayOfMonthlyReminderRequest;
import be.ehb.model.requests.UpdateMailTemplateRequest;
import be.ehb.model.responses.DayOfMonthlyReminderResponse;
import be.ehb.model.responses.MailTemplateResponse;
import be.ehb.model.responses.SystemComponentsStatus;
import be.ehb.model.responses.SystemStatusResponse;
import be.ehb.security.idp.IIdpClient;
import be.ehb.storage.IStorageService;
import org.apache.commons.lang3.StringUtils;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@Default
public class SystemFacade implements ISystemFacade {

    @Inject
    private IStorageService storage;
    @Inject
    private IIdpClient idpClient;
    @Inject
    private IAppConfig appConfig;

    public SystemStatusResponse getStatus() {
        SystemStatusResponse rval = new SystemStatusResponse();
        rval.setId(ID);
        rval.setName(NAME);
        rval.setDescription(DESCRIPTION);
        rval.setMoreInfo(MORE_INFO);
        rval.setBuiltOn(appConfig.getBuildDate());
        rval.setVersion(appConfig.getVersion());
        SystemComponentsStatus compStatus = new SystemComponentsStatus();
        compStatus.setTimeskipApi(storage != null);
        compStatus.setKeycloakIdp(idpClient.getPublicKey(appConfig.getIdpRealm(), appConfig.getIdpKeystoreId()) != null);
        rval.setComponentsUp(compStatus);
        rval.setUp(compStatus.getKeycloakIdp() && compStatus.getTimeskipApi());
        return rval;
    }

    @Override
    public List<MailTemplateResponse> listMailTemplates() {
        return storage.listMailTemplates().stream().map(ResponseFactory::createMailTemplateResponse).collect(Collectors.toList());
    }

    @Override
    public MailTemplateResponse getMailTemplate(MailTopic topic) {
        return ResponseFactory.createMailTemplateResponse(storage.getMailTemplate(topic));
    }

    @Override
    public MailTemplateResponse updateMailTemplate(MailTopic topic, UpdateMailTemplateRequest request) {
        MailTemplateBean template = storage.getMailTemplate(topic);
        boolean changed = false;
        if (StringUtils.isNotEmpty(request.getSubject()) || !request.getSubject().equals(template.getSubject())) {
            template.setSubject(request.getSubject());
            changed = true;
        }
        if (StringUtils.isNotEmpty(request.getContent()) || !request.getContent().equals(template.getContent())) {
            template.setContent(request.getContent());
            changed = true;
        }
        if (changed) {
            return ResponseFactory.createMailTemplateResponse(storage.updateMailTemplate(template));
        } else return null;
    }

    @Override
    public DayOfMonthlyReminderResponse getDayOfMonthlyReminder() {
        ConfigBean config = storage.getDefaultConfig();
        return ResponseFactory.createDayOfMonthlyReminderResponse(config.getDayOfMonthlyReminderEmail(), config.getLastDayOfMonth());
    }

    @Override
    public DayOfMonthlyReminderResponse updateDayOfMonthlyReminder(UpdateDayOfMonthlyReminderRequest request) {
        ConfigBean config = storage.getDefaultConfig();
        boolean changed = false;
        if (request.getLastDayOfMonth() != null && request.getLastDayOfMonth() != config.getLastDayOfMonth() && (config.getDayOfMonthlyReminderEmail() != null || request.getDayOfMonthlyReminder() != null)) {
            config.setLastDayOfMonth(request.getLastDayOfMonth());
            changed = true;
        }
        if ((request.getDayOfMonthlyReminder() != null && !Objects.equals(request.getDayOfMonthlyReminder(), config.getDayOfMonthlyReminderEmail())) || config.getLastDayOfMonth()) {
            config.setDayOfMonthlyReminderEmail(request.getDayOfMonthlyReminder());
            changed = true;
        }
        if (changed) {
            config = storage.updateConfig(config);
            return ResponseFactory.createDayOfMonthlyReminderResponse(config.getDayOfMonthlyReminderEmail(), config.getLastDayOfMonth());
        } else return null;
    }
}