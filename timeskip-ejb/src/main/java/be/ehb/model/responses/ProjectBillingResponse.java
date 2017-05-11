package be.ehb.model.responses;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProjectBillingResponse implements Serializable {

    private ProjectResponse project;
    private BigDecimal totalBillableHours;
    private BigDecimal totalAmountDue;
    private List<ActivityBillingResponse> activities;

    public ProjectResponse getProject() {
        return project;
    }

    public void setProject(ProjectResponse project) {
        this.project = project;
    }

    public List<ActivityBillingResponse> getActivities() {
        return activities;
    }

    public void setActivities(List<ActivityBillingResponse> activities) {
        this.activities = activities;
    }

    public BigDecimal getTotalAmountDue() {
        return totalAmountDue;
    }

    public void setTotalAmountDue(BigDecimal totalAmountDue) {
        this.totalAmountDue = totalAmountDue;
    }

    public BigDecimal getTotalBillableHours() {
        return totalBillableHours;
    }

    public void setTotalBillableHours(BigDecimal totalBillableHours) {
        this.totalBillableHours = totalBillableHours;
    }

    @Override
    public String toString() {
        return "ProjectBillingResponse{" +
                "project=" + project +
                ", activities=" + activities +
                ", totalAmountDue=" + totalAmountDue +
                ", totalBillableHours=" + totalBillableHours +
                '}';
    }
}