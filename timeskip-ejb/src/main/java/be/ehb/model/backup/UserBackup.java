package be.ehb.model.backup;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.util.Set;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserBackup implements Serializable {

    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private Boolean admin;
    private Long paygradeId;
    private Double defaultHoursPerDay;
    private Long defaultActivityId;
    private Set<DayOfWeek> workDays;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getAdmin() {
        return admin;
    }

    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }

    public Long getPaygradeId() {
        return paygradeId;
    }

    public void setPaygradeId(Long paygradeId) {
        this.paygradeId = paygradeId;
    }

    public Double getDefaultHoursPerDay() {
        return defaultHoursPerDay;
    }

    public void setDefaultHoursPerDay(Double defaultHoursPerDay) {
        this.defaultHoursPerDay = defaultHoursPerDay;
    }

    public Long getDefaultActivityId() {
        return defaultActivityId;
    }

    public void setDefaultActivityId(Long defaultActivityId) {
        this.defaultActivityId = defaultActivityId;
    }

    public Set<DayOfWeek> getWorkDays() {
        return workDays;
    }

    public void setWorkDays(Set<DayOfWeek> workDays) {
        this.workDays = workDays;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserBackup)) return false;

        UserBackup that = (UserBackup) o;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "UserBackup{" +
                "id='" + id + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", admin=" + admin +
                ", paygradeId=" + paygradeId +
                ", defaultHoursPerDay=" + defaultHoursPerDay +
                ", defaultActivityId=" + defaultActivityId +
                ", workDays=" + workDays +
                '}';
    }
}