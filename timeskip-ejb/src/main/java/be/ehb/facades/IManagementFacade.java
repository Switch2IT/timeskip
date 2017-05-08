package be.ehb.facades;

import be.ehb.mail.MailTopic;
import be.ehb.model.requests.NewPaygradeRequest;
import be.ehb.model.requests.UpdateDayOfMonthlyReminderRequest;
import be.ehb.model.requests.UpdateMailTemplateRequest;
import be.ehb.model.requests.UpdatePaygradeRequest;
import be.ehb.model.responses.DayOfMonthlyReminderResponse;
import be.ehb.model.responses.MailTemplateResponse;
import be.ehb.model.responses.MembershipResponse;
import be.ehb.model.responses.PaygradeResponse;

import java.util.List;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public interface IManagementFacade {

    // MEMBERSHIP RELATED

    MembershipResponse createMembership(String userId, String organizationId, String roleId);

    // MAIL TEMPLATE RELATED

    List<MailTemplateResponse> listMailTemplates();

    MailTemplateResponse getMailTemplate(MailTopic topic);

    MailTemplateResponse updateMailTemplate(MailTopic topic, UpdateMailTemplateRequest request);

    // PAYGRADE RELATED

    List<PaygradeResponse> listPaygrades();

    PaygradeResponse getPaygrade(Long paygradeId);

    PaygradeResponse createPaygrade(NewPaygradeRequest request);

    PaygradeResponse updatePaygrade(Long paygradeId, UpdatePaygradeRequest request);

    void deletePaygrade(Long paygradeId);

    // SCHEDULE RELATED

    DayOfMonthlyReminderResponse getDayOfMonthlyReminder();

    DayOfMonthlyReminderResponse updateDayOfMonthlyReminder(UpdateDayOfMonthlyReminderRequest request);
}
