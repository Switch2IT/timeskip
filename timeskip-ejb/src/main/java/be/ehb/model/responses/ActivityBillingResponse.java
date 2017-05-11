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
public class ActivityBillingResponse implements Serializable {

    private ActivityResponse activity;
    private BigDecimal totalBillableHours;
    private BigDecimal totalAmountDue;
    private List<UserBillingResponse> users;

    public ActivityResponse getActivity() {
        return activity;
    }

    public void setActivity(ActivityResponse activity) {
        this.activity = activity;
    }

    public List<UserBillingResponse> getUsers() {
        return users;
    }

    public void setUsers(List<UserBillingResponse> users) {
        this.users = users;
    }

    public BigDecimal getTotalBillableHours() {
        return totalBillableHours;
    }

    public void setTotalBillableHours(BigDecimal totalBillableHours) {
        this.totalBillableHours = totalBillableHours;
    }

    public BigDecimal getTotalAmountDue() {
        return totalAmountDue;
    }

    public void setTotalAmountDue(BigDecimal totalAmountDue) {
        this.totalAmountDue = totalAmountDue;
    }

    @Override
    public String toString() {
        return "ActivityBillingResponse{" +
                "activity=" + activity +
                ", users=" + users +
                ", totalBillableHours=" + totalBillableHours +
                ", totalAmountDue=" + totalAmountDue +
                '}';
    }
}