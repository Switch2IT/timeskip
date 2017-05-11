package be.ehb.model.responses;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserBillingResponse implements Serializable {

    private UserResponse user;
    private BigDecimal totalBillableHours;
    private BigDecimal totalAmountDue;

    public UserResponse getUser() {
        return user;
    }

    public void setUser(UserResponse user) {
        this.user = user;
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
        return "UserBillingResponse{" +
                "user=" + user +
                ", totalBillableHours=" + totalBillableHours +
                ", totalAmountDue=" + totalAmountDue +
                '}';
    }
}