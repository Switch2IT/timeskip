package be.ehb.model.requests;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateWorklogRequest implements Serializable {

    private Long id;
    private String userId;
    private String day;
    private Long loggedMinutes;
    private Boolean confirmed;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public Long getLoggedMinutes() {
        return loggedMinutes;
    }

    public void setLoggedMinutes(Long loggedMinutes) {
        this.loggedMinutes = loggedMinutes;
    }

    public Boolean getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(Boolean confirmed) {
        this.confirmed = confirmed;
    }

    @Override
    public String toString() {
        return "UpdateWorklogRequest{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", day='" + day + '\'' +
                ", loggedMinutes=" + loggedMinutes +
                ", confirmed=" + confirmed +
                '}';
    }
}