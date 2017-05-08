package be.ehb.facades;

import be.ehb.mail.MailTopic;
import be.ehb.model.requests.UpdateDayOfMonthlyReminderRequest;
import be.ehb.model.requests.UpdateMailTemplateRequest;
import be.ehb.model.responses.DayOfMonthlyReminderResponse;
import be.ehb.model.responses.MailTemplateResponse;
import be.ehb.model.responses.SystemStatusResponse;

import java.util.List;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public interface ISystemFacade {

    String DESCRIPTION = "The Timeskip API allows authenticated users to log minutes on various project activities";
    String ID = "timeskip-api";
    String MORE_INFO = "https://www.erasmushogeschool.be";
    String NAME = "Timeskip REST API";

    // STATUS RELATED

    SystemStatusResponse getStatus();

    // MAIL TEMPLATE RELATED

    List<MailTemplateResponse> listMailTemplates();

    MailTemplateResponse getMailTemplate(MailTopic topic);

    MailTemplateResponse updateMailTemplate(MailTopic topic, UpdateMailTemplateRequest request);

    // SCHEDULE RELATED

    DayOfMonthlyReminderResponse getDayOfMonthlyReminder();

    DayOfMonthlyReminderResponse updateDayOfMonthlyReminder(UpdateDayOfMonthlyReminderRequest request);
}