package be.ehb.scheduler;

import be.ehb.entities.users.UsersWorkLoadActivityBO;
import be.ehb.exceptions.MissingScheduledJobDependenciesException;
import be.ehb.factories.ExceptionFactory;
import be.ehb.mail.IMailService;
import be.ehb.model.mail.ConfirmationReminderMailBean;
import be.ehb.storage.IStorageService;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 *
 * @author Guillaume Vandecasteele/Patrick Van den Bussche
 * @since 2017
 */

public class EmailReminderJob implements Job {

    public static final String MAIL_SERVICE_CONTEXT_KEY = "mailServiceContext";
    public static final String STORAGE_SERVICE_CONTEXT_KEY = "storageServiceContext";

    private static final Logger log = LoggerFactory.getLogger(EmailReminderJob.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        SchedulerContext schedulerContext;
        try {
            schedulerContext = context.getScheduler().getContext();

            if (schedulerContext == null || !schedulerContext.containsKey(MAIL_SERVICE_CONTEXT_KEY) || !schedulerContext.containsKey(STORAGE_SERVICE_CONTEXT_KEY)) {
                throw ExceptionFactory.missingScheduledJobDependenciesException(EmailReminderJob.class.getSimpleName());
            }

            IMailService mailService = (IMailService) schedulerContext.get(MAIL_SERVICE_CONTEXT_KEY);
            IStorageService storage = (IStorageService) schedulerContext.get(STORAGE_SERVICE_CONTEXT_KEY);

            if (mailService == null || storage == null) {
                throw ExceptionFactory.missingScheduledJobDependenciesException(EmailReminderJob.class.getSimpleName());
            }

            List<UsersWorkLoadActivityBO> usersWorkLoadActivityBOList = storage.listUsersWorkloadActivity(new Date());

            if (!usersWorkLoadActivityBOList.isEmpty()) {
                Map<String, List<UsersWorkLoadActivityBO>> sortedMap = new HashMap<>();
                usersWorkLoadActivityBOList.forEach(bo -> {
                    if (sortedMap.containsKey(bo.getId())) {
                        sortedMap.get(bo.getId()).add(bo);
                    } else {
                        List<UsersWorkLoadActivityBO> list = new ArrayList<>();
                        list.add(bo);
                        sortedMap.put(bo.getId(), list);
                    }
                });
                sortedMap.forEach((key, value) -> {
                    ConfirmationReminderMailBean reminder = new ConfirmationReminderMailBean();
                    UsersWorkLoadActivityBO first = value.get(0);
                    reminder.setUserName(first.getFirstName());
                    reminder.setTo(first.getEmail());
                    StringBuilder worklogList = new StringBuilder("<table bgcolor=\"#f6f6f6\">");
                    worklogList.append("<tr style=\"clear: both !important; display: block !important; Margin: 0 auto !important; max-width: 600px !important\">")
                            .append("<td style=\"padding-right:20px\">")
                            .append("<b><i>Date</i></b>")
                            .append("</td>")
                            .append("<td style=\"padding-right:20px\">")
                            .append("<b><i>Hours logged</i></b>")
                            .append("</td>")
                            .append("<td>")
                            .append("<b><i>Project/Action</i></b>")
                            .append("</td>")
                            .append("</tr>");
                    value.forEach(entry -> worklogList
                            .append("<tr style=\"clear: both !important; display: block !important; Margin: 0 auto !important; max-width: 600px !important\">")
                            .append("<td style=\"padding-right:20px\">")
                            .append(new SimpleDateFormat("dd/MM/yyyy").format(entry.getDay()))
                            .append("</td>")
                            .append("<td style=\"padding-right:20px\">")
                            .append("<b>")
                            .append(new BigDecimal(entry.getLoggedMinutes().doubleValue() / 60).setScale(2, BigDecimal.ROUND_HALF_UP))
                            .append("</b>")
                            .append("</td>")
                            .append("<td>")
                            .append(entry.getDescription())
                            .append("</td>")
                            .append("</tr>"));
                    worklogList.append("</table>");
                    reminder.setRequiredWorklogConfirmations(worklogList.toString());
                    mailService.sendConfirmationReminder(reminder);
                });
            }
        } catch (SchedulerException | MissingScheduledJobDependenciesException ex) {
            log.error("Unable to execute scheduled e-mail reminder job: {}", ex);
        }
    }
}
