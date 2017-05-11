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
public class DayBillingResponse implements Serializable {

    private String day;
    private BigDecimal totalBillableHours;
    private BigDecimal totalAmountDue;
    private List<ProjectBillingResponse> projects;

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public List<ProjectBillingResponse> getProjects() {
        return projects;
    }

    public void setProjects(List<ProjectBillingResponse> projects) {
        this.projects = projects;
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
        return "DayBillingResponse{" +
                "day='" + day + '\'' +
                ", projects=" + projects +
                ", totalBillableHours=" + totalBillableHours +
                ", totalAmountDue=" + totalAmountDue +
                '}';
    }
}