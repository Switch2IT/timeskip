package be.ehb.security;

import be.ehb.exceptions.StorageException;
import be.ehb.storage.IStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.HashSet;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public abstract class AbstractSecurityContext implements ISecurityContext, Serializable {

    static Logger log = LoggerFactory.getLogger(AbstractSecurityContext.class);

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
        String userId = getCurrentUser();
        try {
            return new IndexedPermissions(getStorage().getPermissions(userId));
        } catch (StorageException e) {
            log.error("Error loading permissions for user: {}; cause: {}", userId, e);
            return new IndexedPermissions(new HashSet<>());
        }
    }

    void clearPermissions() {
        permissions = null;
    }

    private IStorageService getStorage() {
        return storage;
    }

}