package be.ehb.model.mail;

/**
 * @author Patrick Van den Bussche
 * @since 2017
 */
public class PrefillTimeSheetMailBean extends BaseMailBean {

    private String userName;
    private String prefillWorklog;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPrefillWorklog() {
        return prefillWorklog;
    }

    public void setPrefillWorklog(String prefillWorklog) {
        this.prefillWorklog = prefillWorklog;
    }

    @Override
    public String toString() {
        return "PrefillTimeSheetMailBean{" +
                "prefillWorklog='" + prefillWorklog + '\'' +
                '}';
    }
}