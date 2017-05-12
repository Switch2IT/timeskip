package be.ehb.model.responses;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.util.List;
import java.util.Set;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponse implements Serializable {

    private String id;
    private String email;
    private String firstName;
    private String lastName;
    private Boolean admin;
    private List<MembershipResponse> memberships;
    private PaygradeResponse paygrade;
    private Double defaultHoursPerDay;
    private Set<DayOfWeek> workDays;
    private Long defaultActivity;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public Boolean getAdmin() {
        return admin;
    }

    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }

    public List<MembershipResponse> getMemberships() {
        return memberships;
    }

    public void setMemberships(List<MembershipResponse> memberships) {
        this.memberships = memberships;
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

    public Long getDefaultActivity() {
        return defaultActivity;
    }

    public void setDefaultActivity(Long defaultActivity) {
        this.defaultActivity = defaultActivity;
    }

    public PaygradeResponse getPaygrade() {
        return paygrade;
    }

    public void setPaygrade(PaygradeResponse paygrade) {
        this.paygrade = paygrade;
    }

    @Override
    public String toString() {
        return "UserResponse{" +
                "id='" + id + '\'' +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", admin=" + admin +
                ", memberships=" + memberships +
                ", paygrade=" + paygrade +
                ", defaultHoursPerDay=" + defaultHoursPerDay +
                ", workDays=" + workDays +
                ", defaultActivity=" + defaultActivity +
                '}';
    }
}