package be.ehb.model.requests;

import be.ehb.model.AbstractBaseRequest;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public class NewActivityRequest extends AbstractBaseRequest {

    private Boolean billable;

    public Boolean getBillable() {
        return billable;
    }

    public void setBillable(Boolean billable) {
        this.billable = billable;
    }

    @Override
    public String toString() {
        return "NewActivityRequest{" +
                super.toString() +
                ", billable=" + billable +
                '}';
    }
}