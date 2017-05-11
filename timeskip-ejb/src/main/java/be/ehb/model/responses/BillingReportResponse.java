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
public class BillingReportResponse implements Serializable {

    private List<OrganizationBillingResponse> organizations;
    private BigDecimal totalBillableHours;
    private BigDecimal totalAmountDue;

    public List<OrganizationBillingResponse> getOrganizations() {
        return organizations;
    }

    public void setOrganizations(List<OrganizationBillingResponse> organizations) {
        this.organizations = organizations;
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
        return "BillingReportResponse{" +
                "organizations=" + organizations +
                ", totalBillableHours=" + totalBillableHours +
                ", totalAmountDue=" + totalAmountDue +
                '}';
    }
}