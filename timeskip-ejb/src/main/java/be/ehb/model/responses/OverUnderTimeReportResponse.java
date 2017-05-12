package be.ehb.model.responses;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.List;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OverUnderTimeReportResponse implements Serializable {

    private List<UserWorkDayResponse> userWorkdays;

    public List<UserWorkDayResponse> getUserWorkdays() {
        return userWorkdays;
    }

    public void setUserWorkdays(List<UserWorkDayResponse> userWorkdays) {
        this.userWorkdays = userWorkdays;
    }

    @Override
    public String toString() {
        return "OverUnderTimeReportResponse{" +
                "userWorkdays=" + userWorkdays +
                '}';
    }
}