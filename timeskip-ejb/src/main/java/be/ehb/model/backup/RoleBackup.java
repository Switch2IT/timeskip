package be.ehb.model.backup;

import be.ehb.model.AbstractBaseStringId;
import be.ehb.security.PermissionType;

import java.util.Set;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public class RoleBackup extends AbstractBaseStringId {

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
        return "RoleBackup{" +
                super.toString() +
                ", autoGrant=" + autoGrant +
                ", permissions=" + permissions +
                '}';
    }
}