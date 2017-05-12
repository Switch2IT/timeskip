package be.ehb.scheduler;

import be.ehb.entities.users.UsersWorkLoadActivityBO;
import be.ehb.mail.IMailService;
import be.ehb.model.mail.ConfirmationReminderMailBean;
import be.ehb.storage.IStorageService;
import org.joda.time.LocalDate;
import org.quartz.JobExecutionException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Patrick Van den Bussche
 * @since 2017
 */
class EmailReminderJobContext {

    private IMailService mailService;
    private IStorageService iss;

    void setMailService(IMailService mailService) {
        this.mailService = mailService;
    }

    void setIss(IStorageService iss) {
        this.iss = iss;
    }

    void execute() throws JobExecutionException {
        List<UsersWorkLoadActivityBO> usersWorkLoadActivityBOList = iss.listUsersWorkloadActivity(new LocalDate());

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
                StringBuilder worklogList = new StringBuilder("<ul>");
                value.forEach(entry -> worklogList.append("<li><b>")
                        .append(entry.getDescription())
                        .append("</b>: ")
                        .append(entry.getDay())
                        .append(" - ")
                        .append(new BigDecimal(entry.getLoggedMinutes().doubleValue() / 60).setScale(2, BigDecimal.ROUND_HALF_UP))
                        .append(" hours logged. ")
                        .append(entry.getConfirmed() ? "Already " : "Not yet ")
                        .append("confirmed.")
                        .append("</li>"));
                worklogList.append("</ul>");
                reminder.setRequiredWorklogConfirmations(worklogList.toString());
                mailService.sendConfirmationReminder(reminder);
            });
        }
    }
}
