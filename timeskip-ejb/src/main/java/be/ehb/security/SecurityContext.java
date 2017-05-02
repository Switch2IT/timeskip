package be.ehb.security;

import be.ehb.entities.identity.UserBean;
import be.ehb.facades.IUserFacade;
import be.ehb.facades.UserFacade;
import org.apache.commons.lang3.StringUtils;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.MalformedClaimException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.Set;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
@SessionScoped
@Default
public class SecurityContext implements ISecurityContext, Serializable {

    private static final Logger log = LoggerFactory.getLogger(SecurityContext.class);

    @Inject
    private IUserFacade userFacade;
    private String currentUser;

    @Override
    public String getCurrentUser() {
        if (!StringUtils.isEmpty(currentUser)) {
            UserBean userName = userFacade.get(currentUser);
            if (userName == null) {
                currentUser = "";
            }
        } else {
            currentUser = "";
        }
        return currentUser;
    }

    @Override
    public String setCurrentUser(JwtClaims claims) {
        return null;
    }

    @Override
    public String setCurrentUser(String userName) {
        return null;
    }

    @Override
    public String getFullName() {
        return null;
    }

    @Override
    public String getEmail() {
        return null;
    }

    @Override
    public boolean isAdmin() {
        return false;
    }

    @Override
    public boolean hasPermission(PermissionType permission, String organizationId) {
        return false;
    }

    @Override
    public Set<String> getPermittedOrganizations(PermissionType permission) {
        return null;
    }
}