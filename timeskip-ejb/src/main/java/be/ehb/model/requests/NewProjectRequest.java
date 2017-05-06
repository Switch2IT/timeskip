package be.ehb.model.requests;

import be.ehb.model.AbstractBaseRequest;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public class NewProjectRequest extends AbstractBaseRequest {

    private Boolean allowOvertime;
    private Boolean billOvertime;

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

    @Override
    public String toString() {
        return "NewProjectRequest{" +
                super.toString() +
                ", allowOvertime=" + allowOvertime +
                ", billOvertime=" + billOvertime +
                '}';
    }
}