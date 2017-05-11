package be.ehb.model.backup;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.joda.time.LocalDate;

import java.io.Serializable;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WorklogBackup implements Serializable {

    private Long id;
    private String userId;
    private Long activityId;
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

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WorklogBackup)) return false;

        WorklogBackup that = (WorklogBackup) o;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "WorklogBackup{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", activityId=" + activityId +
                ", day=" + day +
                ", loggedMinutes=" + loggedMinutes +
                ", confirmed=" + confirmed +
                '}';
    }
}