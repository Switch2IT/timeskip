package be.ehb.facades;

import be.ehb.security.PermissionType;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public interface ISecurityFacade {

    boolean hasPermission(PermissionType permissionType, String entityId);

}
