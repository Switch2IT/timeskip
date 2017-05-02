package be.ehb.security;

import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.MalformedClaimException;

import java.util.Set;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public interface ISecurityContext {

    String getCurrentUser();

    String setCurrentUser(JwtClaims claims);

    String setCurrentUser(String userName);

    String getFullName();

    String getEmail();

    boolean isAdmin();

    boolean hasPermission(PermissionType permission, String organizationId);

    Set<String> getPermittedOrganizations(PermissionType permission);

}