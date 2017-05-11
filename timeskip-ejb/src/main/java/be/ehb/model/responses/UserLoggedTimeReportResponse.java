package be.ehb.model.responses;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserLoggedTimeReportResponse implements Serializable {

    private UserResponse user;
    private LoggedTimeReportResponse report;

    public UserResponse getUser() {
        return user;
    }

    public void setUser(UserResponse user) {
        this.user = user;
    }

    public LoggedTimeReportResponse getReport() {
        return report;
    }

    public void setReport(LoggedTimeReportResponse report) {
        this.report = report;
    }

    @Override
    public String toString() {
        return "UserLoggedTimeReportResponse{" +
                "user=" + user +
                ", report=" + report +
                '}';
    }
}