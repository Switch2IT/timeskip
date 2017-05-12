package be.ehb.model.responses;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.List;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserWorkDayResponse implements Serializable {

    private UserResponse user;
    private List<WorkDayResponse> workdays;

    public UserResponse getUser() {
        return user;
    }

    public void setUser(UserResponse user) {
        this.user = user;
    }

    public List<WorkDayResponse> getWorkdays() {
        return workdays;
    }

    public void setWorkdays(List<WorkDayResponse> workdays) {
        this.workdays = workdays;
    }

    @Override
    public String toString() {
        return "UserWorkDayResponse{" +
                "user=" + user +
                ", workdays=" + workdays +
                '}';
    }
}