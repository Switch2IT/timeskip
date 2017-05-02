package be.ehb.facades;

import be.ehb.security.Permission;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public interface ISecurityFacade {

    boolean hasPermission(Permission permission, String entityId);

}
