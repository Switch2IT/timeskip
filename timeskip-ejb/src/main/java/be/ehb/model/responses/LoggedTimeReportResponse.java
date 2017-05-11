package be.ehb.model.responses;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.List;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoggedTimeReportResponse implements Serializable {

    private List<OrganizationLoggedTimeResponse> organizations;
    private Long totalLoggedMinutes;

    public List<OrganizationLoggedTimeResponse> getOrganizations() {
        return organizations;
    }

    public void setOrganizations(List<OrganizationLoggedTimeResponse> organizations) {
        this.organizations = organizations;
    }

    public Long getTotalLoggedMinutes() {
        return totalLoggedMinutes;
    }

    public void setTotalLoggedMinutes(Long totalLoggedMinutes) {
        this.totalLoggedMinutes = totalLoggedMinutes;
    }

    @Override
    public String toString() {
        return "LoggedTimeReportResponse{" +
                "organizations=" + organizations +
                ", totalLoggedMinutes=" + totalLoggedMinutes +
                '}';
    }
}