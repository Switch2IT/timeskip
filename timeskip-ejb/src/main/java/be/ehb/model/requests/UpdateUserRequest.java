package be.ehb.model.requests;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.util.Set;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateUserRequest implements Serializable {

    private String email;
    private String firstName;
    private String lastName;
    private Double defaultHoursPerDay;
    private Set<DayOfWeek> workDays;
    private Long paygradeId;
    private Long defaultActivity;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Double getDefaultHoursPerDay() {
        return defaultHoursPerDay;
    }

    public void setDefaultHoursPerDay(Double defaultHoursPerDay) {
        this.defaultHoursPerDay = defaultHoursPerDay;
    }

    public Set<DayOfWeek> getWorkDays() {
        return workDays;
    }

    public void setWorkDays(Set<DayOfWeek> workDays) {
        this.workDays = workDays;
    }

    public Long getPaygradeId() {
        return paygradeId;
    }

    public void setPaygradeId(Long paygradeId) {
        this.paygradeId = paygradeId;
    }

    public Long getDefaultActivity() {
        return defaultActivity;
    }

    public void setDefaultActivity(Long defaultActivity) {
        this.defaultActivity = defaultActivity;
    }

    @Override
    public String toString() {
        return "UpdateUserRequest{" +
                "email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", defaultHoursPerDay=" + defaultHoursPerDay +
                ", workDays=" + workDays +
                ", paygradeId=" + paygradeId +
                ", defaultActivity=" + defaultActivity +
                '}';
    }
}