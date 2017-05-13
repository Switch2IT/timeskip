package be.ehb.model.responses;

import be.ehb.security.PermissionType;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.List;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoleResponse implements Serializable {

    private String id;
    private String name;
    private String description;
    private Boolean autoGrant;
    private List<PermissionType> permissions;

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public Boolean getAutoGrant() {
        return autoGrant;
    }

    public void setAutoGrant(Boolean autoGrant) {
        this.autoGrant = autoGrant;
    }

    public List<PermissionType> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<PermissionType> permissions) {
        this.permissions = permissions;
    }

    @Override
    public String toString() {
        return "RoleResponse{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", autoGrant=" + autoGrant +
                ", permissions=" + permissions +
                '}';
    }
}