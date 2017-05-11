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
public class OrganizationBillingResponse implements Serializable {

    private OrganizationResponse organization;
    private BigDecimal totalBillableHours;
    private BigDecimal totalAmountDue;
    private List<DayBillingResponse> days;

    public OrganizationResponse getOrganization() {
        return organization;
    }

    public void setOrganization(OrganizationResponse organization) {
        this.organization = organization;
    }

    public List<DayBillingResponse> getDays() {
        return days;
    }

    public void setDays(List<DayBillingResponse> days) {
        this.days = days;
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
        return "OrganizationBillingResponse{" +
                "organization=" + organization +
                ", days=" + days +
                ", totalAmountDue=" + totalAmountDue +
                ", totalBillableHours=" + totalBillableHours +
                '}';
    }
}