package be.ehb.entities.projects;

import be.ehb.entities.organizations.OrganizationBean;
import be.ehb.entities.users.UserBean;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.List;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
@Entity
@Table(name = "projects", schema = "timeskip")
public class ProjectBean {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "name", nullable = false)
    private String name;
    @Lob
    @Column(name = "description")
    @Type(type = "org.hibernate.type.TextType")
    private String description;
    @Column(name = "allow_overtime")
    private Boolean allowOvertime;
    @Column(name = "bill_overtime")
    private Boolean billOvertime;
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "project_assignments", joinColumns = @JoinColumn(name = "project_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<UserBean> assignedUsers;
    @ManyToOne
    @JoinColumn(name = "organization_id", referencedColumnName = "id", nullable = false)
    private OrganizationBean organization;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getAllowOvertime() {
        return allowOvertime;
    }

    public void setAllowOvertime(Boolean allowOvertime) {
        this.allowOvertime = allowOvertime;
    }

    public Boolean getBillOvertime() {
        return billOvertime;
    }

    public void setBillOvertime(Boolean billOvertime) {
        this.billOvertime = billOvertime;
    }

    public List<UserBean> getAssignedUsers() {
        return assignedUsers;
    }

    public void setAssignedUsers(List<UserBean> assignedUsers) {
        this.assignedUsers = assignedUsers;
    }

    public OrganizationBean getOrganization() {
        return organization;
    }

    public void setOrganization(OrganizationBean organization) {
        this.organization = organization;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProjectBean)) return false;

        ProjectBean that = (ProjectBean) o;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "ProjectsBean{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", allowOvertime=" + allowOvertime +
                ", billOvertime=" + billOvertime +
                ", assignedUsers=" + assignedUsers +
                ", organization=" + organization +
                '}';
    }
}