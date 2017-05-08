package be.ehb.entities.security;

import be.ehb.security.PermissionType;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
@Entity
@Table(name = "roles", schema = "timeskip")
public class RoleBean implements Serializable {

    @Id
    @Column(name = "id", nullable = false)
    private String id;
    @Column(name = "auto_grant")
    private Boolean autoGrant;
    @Lob
    @Column(name = "description")
    @Type(type = "org.hibernate.type.TextType")
    private String description;
    @Column(name = "name")
    private String name;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "permissions", joinColumns = @JoinColumn(name = "role_id"))
    @Column(name = "permission")
    private List<PermissionType> permissions;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getAutoGrant() {
        return autoGrant;
    }

    public void setAutoGrant(Boolean autoGrant) {
        this.autoGrant = autoGrant;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<PermissionType> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<PermissionType> permissions) {
        this.permissions = permissions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RoleBean)) return false;

        RoleBean roleBean = (RoleBean) o;

        return id.equals(roleBean.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "RoleBean{" +
                "id='" + id + '\'' +
                ", autoGrant=" + autoGrant +
                ", description='" + description + '\'' +
                ", name='" + name + '\'' +
                ", permissions=" + permissions +
                '}';
    }
}