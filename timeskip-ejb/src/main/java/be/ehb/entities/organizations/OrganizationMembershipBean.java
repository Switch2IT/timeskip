package be.ehb.entities.organizations;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
@Entity
@Table(name = "organization_memberships")
public class OrganizationMembershipBean implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "organization_id")
    private String organizationId;
    @Column(name = "role_id")
    private String roleId;
    @Column(name = "user_id")
    private String userId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrganizationMembershipBean)) return false;

        OrganizationMembershipBean that = (OrganizationMembershipBean) o;

        return id != null ? id.equals(that.id) : that.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "MembershipBean{" +
                "id=" + id +
                ", organizationId='" + organizationId + '\'' +
                ", roleId='" + roleId + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}