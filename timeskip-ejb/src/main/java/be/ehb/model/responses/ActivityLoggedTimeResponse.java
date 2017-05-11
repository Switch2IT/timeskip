package be.ehb.model.responses;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ActivityLoggedTimeResponse {

    private ActivityResponse activity;
    private Long totalLoggedMinutes;

    public ActivityResponse getActivity() {
        return activity;
    }

    public void setActivity(ActivityResponse activity) {
        this.activity = activity;
    }

    public Long getTotalLoggedMinutes() {
        return totalLoggedMinutes;
    }

    public void setTotalLoggedMinutes(Long totalLoggedMinutes) {
        this.totalLoggedMinutes = totalLoggedMinutes;
    }

    @Override
    public String toString() {
        return "ActivityLoggedTimeResponse{" +
                "activity=" + activity +
                ", totalLoggedMinutes=" + totalLoggedMinutes +
                '}';
    }
}