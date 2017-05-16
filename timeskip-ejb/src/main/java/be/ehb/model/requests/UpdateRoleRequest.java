package be.ehb.model.requests;

import be.ehb.model.AbstractBaseNoId;
import be.ehb.security.PermissionType;

import java.util.Set;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public class UpdateRoleRequest extends AbstractBaseNoId {

    private Boolean autoGrant;
    private Set<PermissionType> permissions;

    public Boolean getAutoGrant() {
        return autoGrant;
    }

    public void setAutoGrant(Boolean autoGrant) {
        this.autoGrant = autoGrant;
    }

    public Set<PermissionType> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<PermissionType> permissions) {
        this.permissions = permissions;
    }

    @Override
    public String toString() {
        return "UpdateRoleRequest{" +
                super.toString() +
                ", autoGrant=" + autoGrant +
                ", permissions=" + permissions +
                '}';
    }
}