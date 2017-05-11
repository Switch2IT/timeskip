package be.ehb.model.responses;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.List;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrganizationLoggedTimeResponse implements Serializable {

    private OrganizationResponse organization;
    private List<ProjectLoggedTimeResponse> projects;
    private Long totalLoggedMinutes;

    public OrganizationResponse getOrganization() {
        return organization;
    }

    public void setOrganization(OrganizationResponse organization) {
        this.organization = organization;
    }

    public List<ProjectLoggedTimeResponse> getProjects() {
        return projects;
    }

    public void setProjects(List<ProjectLoggedTimeResponse> projects) {
        this.projects = projects;
    }

    public Long getTotalLoggedMinutes() {
        return totalLoggedMinutes;
    }

    public void setTotalLoggedMinutes(Long totalLoggedMinutes) {
        this.totalLoggedMinutes = totalLoggedMinutes;
    }

    @Override
    public String toString() {
        return "OrganizationLoggedTimeResponse{" +
                "organization=" + organization +
                ", projects=" + projects +
                ", totalLoggedMinutes=" + totalLoggedMinutes +
                '}';
    }
}