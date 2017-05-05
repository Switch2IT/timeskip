package be.ehb.model.responses;

import be.ehb.model.AbstractBaseResponse;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public class ProjectResponse extends AbstractBaseResponse {

    private Boolean allowOvertime;
    private Boolean billOvertime;
    private OrganizationResponse organization;

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

    public OrganizationResponse getOrganization() {
        return organization;
    }

    public void setOrganization(OrganizationResponse organization) {
        this.organization = organization;
    }

    @Override
    public String toString() {
        return "ProjectDTO{" +
                super.toString() +
                ", allowOvertime=" + allowOvertime +
                ", billOvertime=" + billOvertime +
                ", organization=" + organization +
                '}';
    }
}