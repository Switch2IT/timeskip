package be.ehb.security;

import be.ehb.entities.users.UserBean;
import be.ehb.facades.IUserFacade;
import be.ehb.factories.ExceptionFactory;
import org.apache.commons.lang3.StringUtils;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.MalformedClaimException;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Default;
import javax.inject.Inject;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
@SessionScoped
@Default
public class SecurityContext extends AbstractSecurityContext {

    @Inject
    private IUserFacade userFacade;
    private String currentUser;

    @Override
    public String getCurrentUser() {
        if (!StringUtils.isEmpty(currentUser)) {
            UserBean userName = userFacade.get(currentUser);
            if (userName == null) {
                currentUser = "";
                clearPermissions();
            }
        } else {
            currentUser = "";
            clearPermissions();
        }
        return currentUser;
    }

    @Override
    public String setCurrentUser(JwtClaims claims) {
        try {
            return setCurrentUser(claims.getSubject() != null ? claims.getSubject() : "");
        } catch (MalformedClaimException ex) {
            throw ExceptionFactory.jwtValidationException("Invalid JWT claims");
        } catch (Exception ex) {
            try {
                userFacade.initNewUser(claims);
                return currentUser;
            } catch (Exception e) {
                log.error("Unable to create new user: {}", e.getMessage());
                throw ExceptionFactory.userNotFoundException(currentUser);
            }
        }
    }

    @Override
    public String setCurrentUser(String userName) {
        this.currentUser = userName;
        return getCurrentUser();
    }

    @Override
    public String getEmail() {
        return userFacade.get(currentUser).getEmail();
    }

    @Override
    public boolean isAdmin() {
        boolean rval = false;
        if (StringUtils.isNotEmpty(currentUser)) {
            UserBean user = userFacade.get(currentUser);
            if (user != null && user.getAdmin() != null) {
                rval = user.getAdmin();
            }
        }
        return rval;
    }
}