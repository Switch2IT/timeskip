package be.ehb.model.responses;

import be.ehb.model.AbstractBaseNumericIdResponse;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public class ActivityResponse extends AbstractBaseNumericIdResponse {

    private Boolean billable;
    private ProjectResponse project;

    public Boolean getBillable() {
        return billable;
    }

    public void setBillable(Boolean billable) {
        this.billable = billable;
    }

    public ProjectResponse getProject() {
        return project;
    }

    public void setProject(ProjectResponse project) {
        this.project = project;
    }

    @Override
    public String toString() {
        return "ActivityResponse{" +
                super.toString() +
                ", billable=" + billable +
                ", project=" + project +
                '}';
    }
}