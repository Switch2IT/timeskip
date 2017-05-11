package be.ehb.model.responses;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.List;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProjectLoggedTimeResponse implements Serializable {

    private ProjectResponse project;
    private List<ActivityLoggedTimeResponse> activities;
    private Long totalLoggedMinutes;

    public ProjectResponse getProject() {
        return project;
    }

    public void setProject(ProjectResponse project) {
        this.project = project;
    }

    public List<ActivityLoggedTimeResponse> getActivities() {
        return activities;
    }

    public void setActivities(List<ActivityLoggedTimeResponse> activities) {
        this.activities = activities;
    }

    public Long getTotalLoggedMinutes() {
        return totalLoggedMinutes;
    }

    public void setTotalLoggedMinutes(Long totalLoggedMinutes) {
        this.totalLoggedMinutes = totalLoggedMinutes;
    }

    @Override
    public String toString() {
        return "ProjectLoggedTimeResponse{" +
                "project=" + project +
                ", activities=" + activities +
                ", totalLoggedMinutes=" + totalLoggedMinutes +
                '}';
    }
}