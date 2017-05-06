package be.ehb.model.requests;

import be.ehb.model.responses.MembershipResponse;
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
public class NewUserRequest implements Serializable {

    private String email;
    private String firstName;
    private String lastName;
    private List<MembershipResponse> memberships;
    private Double defaultHoursPerDay;
    private Set<DayOfWeek> workDays;

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

    @Override
    public String toString() {
        return "NewUserRequest{" +
                "email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", memberships=" + memberships +
                ", defaultHoursPerDay=" + defaultHoursPerDay +
                ", workDays=" + workDays +
                '}';
    }
}