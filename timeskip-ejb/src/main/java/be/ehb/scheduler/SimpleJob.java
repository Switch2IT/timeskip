package be.ehb.scheduler;

import be.ehb.mail.BaseMailBean;
import be.ehb.mail.MailProvider;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import javax.mail.internet.MimeMessage;

/**
 * Created by Patrick Van den Bussche on 7/05/2017.
 */
public class SimpleJob implements Job{

    static final String NEW_LINE = "\n";

    public void execute(JobExecutionContext context) throws JobExecutionException {
        String jobName = context.getJobDetail().getDescription();
        //TODO change to send email with uncompleted tasks
        System.out.println("*** SimpleJob **** - " + jobName);

        BaseMailBean bmb = new BaseMailBean();
        //TODO insert real address
        bmb.setTo("pat.vdbussche@gmail.com");
        bmb.setSubject("TimeSkip - Monthly uncompleted tasks");
        StringBuilder content = new StringBuilder();
        //TODO insert real name
        content.append("Dear Patrick");
        content.append(NEW_LINE);
        content.append("Please check following uncompleted tasks for last month");
        content.append(NEW_LINE);
        //TODO insert real not confirmed worklog
        content.append("-- db lijnen --");
        content.append(NEW_LINE);
        content.append("Best regards,");
        content.append(NEW_LINE);
        //TODO insert real name / company
        content.append("Hr Manager of the Company");
        content.append(NEW_LINE);
        content.append("X-X");
        content.append(NEW_LINE);
        content.append("*** This is an automatically generated email, please do not reply ***");
        bmb.setContent(content.toString());
        MailProvider mp = new MailProvider();
        MimeMessage mm = mp.composeMessage(bmb);
        mp.sendMail(mm);

    }
}
