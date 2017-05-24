package be.ehb.security;

import be.ehb.storage.IStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.Serializable;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public abstract class AbstractSecurityContext implements ISecurityContext, Serializable {

    protected static Logger log = LoggerFactory.getLogger(AbstractSecurityContext.class);

    private IndexedPermissions permissions;

    @Inject
    private IStorageService storage;

    @Override
    public boolean hasPermission(PermissionType permission, String organizationId) {
        return isAdmin() || getPermissions().hasQualifiedPermission(permission, organizationId);
    }

    private IndexedPermissions getPermissions() {
        IndexedPermissions rval = permissions;
        if (rval == null) {
            rval = loadPermissions();
            permissions = rval;
        }
        return rval;
    }

    private IndexedPermissions loadPermissions() {
        return new IndexedPermissions(getStorage().getPermissions(getCurrentUser()));
    }

    protected void clearPermissions() {
        permissions = null;
    }

    private IStorageService getStorage() {
        return storage;
    }

}