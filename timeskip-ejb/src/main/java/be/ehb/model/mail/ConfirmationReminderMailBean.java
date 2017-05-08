package be.ehb.model.mail;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public class ConfirmationReminderMailBean extends BaseMailBean {

    private String userName;
    private String requiredWorklogConfirmations;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRequiredWorklogConfirmations() {
        return requiredWorklogConfirmations;
    }

    public void setRequiredWorklogConfirmations(String requiredWorklogConfirmations) {
        this.requiredWorklogConfirmations = requiredWorklogConfirmations;
    }

    @Override
    public String toString() {
        return "ConfirmationReminderMailBean{" +
                "requiredWorklogConfirmations='" + requiredWorklogConfirmations + '\'' +
                '}';
    }
}