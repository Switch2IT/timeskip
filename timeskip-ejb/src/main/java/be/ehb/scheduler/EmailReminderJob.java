package be.ehb.scheduler;

import be.ehb.entities.users.UsersWorkLoadActivityBO;
import be.ehb.mail.BaseMailBean;
import be.ehb.mail.IMailProvider;
import be.ehb.storage.IStorageService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import javax.inject.Inject;
import javax.mail.internet.MimeMessage;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Patrick Van den Bussche on 7/05/2017.
 */

public class EmailReminderJob implements Job {

    private static final String NEW_LINE = "\n";
    @Inject
    private IMailProvider mp;
    @Inject
    private IStorageService iss;

    private static void sendMail(StringBuilder content, BaseMailBean bmb, IMailProvider mp) {
        content.append("Best regards,");
        content.append(NEW_LINE);
        content.append("Hr Manager of the Company");
        content.append(NEW_LINE);
        content.append("X-X");
        content.append(NEW_LINE);
        content.append("*** This is an automatically generated email, please do not reply ***");
        bmb.setContent(content.toString());
        MimeMessage mm = mp.composeMessage(bmb);
        mp.sendMail(mm);
    }

    public void execute(JobExecutionContext context) throws JobExecutionException {
        String jobName = context.getJobDetail().getDescription();
        System.out.println("*** EmailReminderJob **** - " + jobName);

        String prevId = null;
        BaseMailBean bmb = new BaseMailBean();

        List<UsersWorkLoadActivityBO> usersWorkLoadActivityBOList = iss.listUsersWorkloadActivity(new Date());

        if (usersWorkLoadActivityBOList.size() > 0) {
            StringBuilder content = new StringBuilder();
            for (UsersWorkLoadActivityBO usersWorkLoadActivityBO : usersWorkLoadActivityBOList) {
                if (usersWorkLoadActivityBO.getId().equals(prevId)) {
                    if (prevId != null) {
                        sendMail(content, bmb, mp);
                    }
                    bmb.setTo(usersWorkLoadActivityBO.getEmail());
                    bmb.setSubject("TimeSkip - Monthly uncompleted tasks");
                    content.append("Dear ");
                    content.append(usersWorkLoadActivityBO.getFirstName());
                    content.append(" ");
                    content.append(usersWorkLoadActivityBO.getLastName());
                    content.append(NEW_LINE);
                    content.append("Please check following uncompleted tasks for last month");
                    content.append(NEW_LINE);
                } else {
                    content.append(new SimpleDateFormat("dd/MM/yyyy").format(usersWorkLoadActivityBO.getDay()));
                    content.append(" ");
                    content.append(usersWorkLoadActivityBO.getDescription());
                    content.append(" ");
                    content.append(new Long(String.format(usersWorkLoadActivityBO.getLoggedMinutes().toString(), "###")));
                    content.append(NEW_LINE);
                }
            }
            sendMail(content, bmb, mp);
        }
    }
}
