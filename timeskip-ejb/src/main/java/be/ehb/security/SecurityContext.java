package be.ehb.security;

import be.ehb.entities.users.UserBean;
import be.ehb.facades.IUserFacade;
import be.ehb.factories.ExceptionFactory;
import be.ehb.storage.IStorageService;
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
    @Inject
    private IStorageService storage;
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
            String currentUser = setCurrentUser(claims.getSubject() != null ? claims.getSubject() : "");
            UserBean cub = userFacade.get(currentUser);
            String firstName = claims.getStringClaimValue(JWTConstants.GIVEN_NAME);
            String lastName = claims.getStringClaimValue(JWTConstants.SURNAME);
            String email = claims.getStringClaimValue(JWTConstants.EMAIL);
            boolean changed = false;
            if (StringUtils.isNotEmpty(firstName) && !firstName.trim().equals(cub.getFirstName().trim())) {
                cub.setFirstName(firstName);
                changed = true;
            }
            if (StringUtils.isNotEmpty(lastName) && !lastName.trim().equals(cub.getLastName().trim())) {
                cub.setLastName(lastName);
                changed = true;
            }
            if (StringUtils.isNotEmpty(email) && !email.trim().equals(cub.getEmail().trim())) {
                cub.setEmail(email);
                changed = true;
            }
            if (changed) storage.updateUser(cub);
            return currentUser;
        } catch (MalformedClaimException ex) {
            throw ExceptionFactory.jwtValidationException("Invalid JWT claims");
        } catch (Exception ex) {
            try {
                userFacade.initNewUser(claims);
                return currentUser;
            } catch (Exception e) {
                getLog().error("Unable to create new user: {}", e.getMessage());
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