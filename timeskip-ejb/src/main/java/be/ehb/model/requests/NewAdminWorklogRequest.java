package be.ehb.model.requests;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public class NewAdminWorklogRequest extends NewWorklogRequest {

    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "NewAdminWorklogRequest{" +
                "day='" + super.getDay() + '\'' +
                ", loggedMinutes=" + super.getLoggedMinutes() +
                ", confirmed=" + super.getConfirmed() +
                ", userId='" + userId + '\'' +
                '}';
    }
}