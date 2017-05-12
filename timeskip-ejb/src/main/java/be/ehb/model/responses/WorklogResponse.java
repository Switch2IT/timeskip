package be.ehb.model.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.joda.time.LocalDate;

import java.io.Serializable;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WorklogResponse implements Serializable {

    private Long id;
    private String userId;
    private ActivityResponse activity;
    private LocalDate day;
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

    public ActivityResponse getActivity() {
        return activity;
    }

    public void setActivity(ActivityResponse activity) {
        this.activity = activity;
    }

    public LocalDate getDay() {
        return day;
    }

    public void setDay(LocalDate day) {
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
        return "WorklogResponse{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", activity=" + activity +
                ", day=" + day +
                ", loggedMinutes=" + loggedMinutes +
                ", confirmed=" + confirmed +
                '}';
    }
}