package be.ehb.entities.users;

import be.ehb.entities.organizations.MembershipBean;

import javax.persistence.*;
import java.io.Serializable;
import java.time.DayOfWeek;
import java.util.List;
import java.util.Set;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
@Entity
@Table(name = "users", schema = "timeskip")
public class UserBean implements Serializable {

    @Id
    @Column(name = "id", nullable = false)
    private String id;
    @Column(name = "first_name", nullable = false)
    private String firstName;
    @Column(name = "last_name", nullable = false)
    private String lastName;
    @Column(name = "email", nullable = false)
    private String email;
    @Column(name = "admin")
    private Boolean admin;
    @Column(name = "default_hours_per_day")
    private Double defaultHoursPerDay;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_workdays", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "week_day")
    @Enumerated(EnumType.STRING)
    private Set<DayOfWeek> workdays;
    @ManyToOne
    @JoinColumn(name = "paygrade_id", referencedColumnName = "id")
    private PaygradeBean paygrade;
    @Column(name = "default_activity_id")
    private Long defaultActivity;
    @OneToMany
    @JoinColumn(name = "user_id")
    private List<MembershipBean> memberships;

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

    public Double getDefaultHoursPerDay() {
        return defaultHoursPerDay;
    }

    public void setDefaultHoursPerDay(Double defaultHoursPerDay) {
        this.defaultHoursPerDay = defaultHoursPerDay;
    }

    public Set<DayOfWeek> getWorkdays() {
        return workdays;
    }

    public void setWorkdays(Set<DayOfWeek> workdays) {
        this.workdays = workdays;
    }

    public PaygradeBean getPaygrade() {
        return paygrade;
    }

    public void setPaygrade(PaygradeBean paygrade) {
        this.paygrade = paygrade;
    }

    public Long getDefaultActivity() {
        return defaultActivity;
    }

    public void setDefaultActivity(Long defaultActivity) {
        this.defaultActivity = defaultActivity;
    }

    public List<MembershipBean> getMemberships() {
        return memberships;
    }

    public void setMemberships(List<MembershipBean> memberships) {
        this.memberships = memberships;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserBean)) return false;

        UserBean userBean = (UserBean) o;

        return id.equals(userBean.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "UserBean{" +
                "id='" + id + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", admin=" + admin +
                ", defaultHoursPerDay=" + defaultHoursPerDay +
                ", workdays=" + workdays +
                ", paygrade=" + paygrade +
                ", defaultActivity=" + defaultActivity +
                '}';
    }
}