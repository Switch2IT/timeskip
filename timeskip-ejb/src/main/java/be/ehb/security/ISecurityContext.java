package be.ehb.security;

import org.jose4j.jwt.JwtClaims;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public interface ISecurityContext {

    String getCurrentUser();

    String setCurrentUser(JwtClaims claims);

    String setCurrentUser(String userName);

    String getEmail();

    boolean isAdmin();

    boolean hasPermission(PermissionType permission, String organizationId);

}