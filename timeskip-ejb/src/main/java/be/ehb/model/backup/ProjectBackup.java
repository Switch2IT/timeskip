package be.ehb.model.backup;

import be.ehb.model.AbstractBaseNumericId;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public class ProjectBackup extends AbstractBaseNumericId {

    private Boolean allowOvertime;
    private Boolean billOvertime;
    private String organizationId;


    public Boolean getAllowOvertime() {
        return allowOvertime;
    }

    public void setAllowOvertime(Boolean allowOvertime) {
        this.allowOvertime = allowOvertime;
    }

    public Boolean getBillOvertime() {
        return billOvertime;
    }

    public void setBillOvertime(Boolean billOvertime) {
        this.billOvertime = billOvertime;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    @Override
    public String toString() {
        return "ProjectBackup{" +
                super.toString() +
                ", allowOvertime=" + allowOvertime +
                ", billOvertime=" + billOvertime +
                ", organizationId='" + organizationId + '\'' +
                '}';
    }
}