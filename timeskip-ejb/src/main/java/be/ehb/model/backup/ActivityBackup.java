package be.ehb.model.backup;

import be.ehb.model.AbstractBaseNumericId;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public class ActivityBackup extends AbstractBaseNumericId {

    private Boolean billable;
    private Long projectId;

    public Boolean getBillable() {
        return billable;
    }

    public void setBillable(Boolean billable) {
        this.billable = billable;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    @Override
    public String toString() {
        return "ActivityBackup{" +
                super.toString() +
                ", billable=" + billable +
                ", projectId=" + projectId +
                '}';
    }
}