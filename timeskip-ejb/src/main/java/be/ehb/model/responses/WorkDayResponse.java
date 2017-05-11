package be.ehb.model.responses;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WorkDayResponse implements Serializable {

    private String day;
    private Long loggedMinutes;

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

    @Override
    public String toString() {
        return "WorkDayResponse{" +
                "day='" + day + '\'' +
                ", loggedMinutes='" + loggedMinutes + '\'' +
                '}';
    }
}