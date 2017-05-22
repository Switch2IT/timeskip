package be.ehb.model.requests;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class NewWorklogRequest implements Serializable {

    private String day;
    private Long loggedMinutes;
    private Boolean confirmed;

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
        return "NewWorklogRequest{" +
                "day='" + day + '\'' +
                ", loggedMinutes=" + loggedMinutes +
                ", confirmed=" + confirmed +
                '}';
    }
}