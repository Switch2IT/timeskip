package be.ehb.facades;

import be.ehb.entities.config.ConfigBean;
import be.ehb.entities.mail.MailTemplateBean;
import be.ehb.entities.organizations.MembershipBean;
import be.ehb.entities.users.PaygradeBean;
import be.ehb.factories.ExceptionFactory;
import be.ehb.factories.ResponseFactory;
import be.ehb.mail.MailTopic;
import be.ehb.model.requests.NewPaygradeRequest;
import be.ehb.model.requests.UpdateDayOfMonthlyReminderRequest;
import be.ehb.model.requests.UpdateMailTemplateRequest;
import be.ehb.model.requests.UpdatePaygradeRequest;
import be.ehb.model.responses.DayOfMonthlyReminderResponse;
import be.ehb.model.responses.MailTemplateResponse;
import be.ehb.model.responses.MembershipResponse;
import be.ehb.model.responses.PaygradeResponse;
import be.ehb.scheduler.IScheduleService;
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
public class ManagementFacade implements IManagementFacade {

    @Inject
    private IStorageService storage;
    @Inject
    private IScheduleService scheduleService;

    @Override
    public MembershipResponse updateOrCreateMembership(String userId, String organizationId, String roleId) {
        //Check if entities exist
        storage.getOrganization(organizationId);
        storage.getUser(userId);
        storage.getRole(roleId);
        MembershipBean newMembership = new MembershipBean();
        newMembership.setRoleId(roleId);
        newMembership.setUserId(userId);
        newMembership.setOrganizationId(organizationId);
        return ResponseFactory.createMembershipResponse(storage.createOrUpdateMembership(newMembership));
    }

    @Override
    public List<MembershipResponse> listUserMemberships(String userId) {
        storage.getUser(userId);
        return storage.listUserMemberships(userId).stream().map(ResponseFactory::createMembershipResponse).collect(Collectors.toList());
    }

    @Override
    public void deleteUserMembership(String userId, String organizationId) {
        storage.getUser(userId);
        storage.getOrganization(organizationId);
        MembershipBean membership = storage.findMembershipByUserAndOrganization(userId, organizationId);
        storage.deleteMembership(membership);
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
            scheduleService.restartEmailReminderJob();
            return ResponseFactory.createDayOfMonthlyReminderResponse(config.getDayOfMonthlyReminderEmail(), config.getLastDayOfMonth());
        } else return null;
    }

    @Override
    public List<PaygradeResponse> listPaygrades() {
        return storage.listPaygrades().stream().map(ResponseFactory::createPaygradeResponse).collect(Collectors.toList());
    }

    @Override
    public PaygradeResponse getPaygrade(Long paygradeId) {
        return ResponseFactory.createPaygradeResponse(storage.getPaygrade(paygradeId));
    }

    @Override
    public PaygradeResponse createPaygrade(NewPaygradeRequest request) {
        if (storage.findPaygradeByName(request.getName()) != null) {
            throw ExceptionFactory.paygradeAlreadyExists(request.getName());
        }
        PaygradeBean newPaygrade = new PaygradeBean();
        newPaygrade.setName(request.getName());
        newPaygrade.setDescription(request.getDescription());
        newPaygrade.setHourlyRate(request.getHourlyRate());
        return ResponseFactory.createPaygradeResponse(storage.createPaygrade(newPaygrade));
    }

    @Override
    public PaygradeResponse updatePaygrade(Long paygradeId, UpdatePaygradeRequest request) {
        PaygradeBean paygrade = storage.getPaygrade(paygradeId);
        boolean changed = false;
        if (StringUtils.isNotEmpty(request.getName()) && !request.getName().equals(paygrade.getName())) {
            if (storage.findPaygradeByName(request.getName()) != null)
                throw ExceptionFactory.paygradeAlreadyExists(request.getName());
            paygrade.setName(request.getName());
            changed = true;
        }
        if (StringUtils.isNotEmpty(request.getDescription()) && !request.getDescription().equals(paygrade.getDescription())) {
            paygrade.setDescription(request.getDescription());
            changed = true;
        }
        if (request.getHourlyRate() != null && !request.getHourlyRate().equals(paygrade.getHourlyRate())) {
            paygrade.setHourlyRate(request.getHourlyRate());
            changed = true;
        }
        if (changed) {
            return ResponseFactory.createPaygradeResponse(storage.updatePaygrade(paygrade));
        } else return null;
    }

    @Override
    public void deletePaygrade(Long paygradeId) {
        PaygradeBean paygrade = storage.getPaygrade(paygradeId);
        if (!storage.findUsersByPaygrade(paygradeId).isEmpty())
            throw ExceptionFactory.paygradeStillInUseException(paygradeId);
        storage.deletePaygrade(paygrade);
    }
}