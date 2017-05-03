package be.ehb.facades;

import be.ehb.entities.identity.UserBean;
import be.ehb.factories.ExceptionFactory;
import be.ehb.model.requests.JWTParseRequest;
import be.ehb.model.responses.TokenClaimsResponse;
import be.ehb.security.ISecurityContext;
import be.ehb.security.JWTConstants;
import be.ehb.security.JWTValidation;
import be.ehb.storage.IStorageService;
import org.apache.commons.lang3.StringUtils;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.*;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import java.io.Serializable;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@Default
public class UserFacade implements IUserFacade, Serializable {

    private static final Logger log = LoggerFactory.getLogger(UserFacade.class);

    @Inject
    private JWTValidation jwtValidation;
    @Inject
    private IStorageService storage;
    @Inject
    private ISecurityContext securityContext;

    @Override
    public UserBean get(String userId) {
        UserBean rval = storage.getUser(userId);
        if (rval == null) {
            throw ExceptionFactory.userNotFoundException(userId);
        }
        return rval;
    }

    @Override
    public TokenClaimsResponse parseJWT(JWTParseRequest jwt) {
        TokenClaimsResponse rVal = null;
        if (StringUtils.isNotEmpty(jwt.getJwt())) {
            try {
                JwtClaims claims = jwtValidation
                        .getUnvalidatedContext(jwt.getJwt()).getJwtClaims();
                TokenClaimsResponse user = new TokenClaimsResponse();
                user.setUserInfo(claims.getClaimsMap());
                rVal = user;
            } catch (InvalidJwtException | MalformedClaimException e) {
                log.error("Failed to parse JWT: {}", jwt);
                e.printStackTrace();

            }
        }
        return rVal;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public UserBean initNewUser(JwtClaims claims) {
        log.info("Init new user with attributes:{}", claims);
        try {
            //create user
            UserBean newUser = new UserBean();
            newUser.setId(claims.getSubject());
            if (!claims.hasClaim(JWTConstants.NAME) && claims.hasClaim(JWTConstants.GIVEN_NAME) && claims.hasClaim(JWTConstants.SURNAME)) {
                newUser.setFullName(claims.getStringClaimValue(JWTConstants.GIVEN_NAME) + " " + claims.getStringClaimValue(JWTConstants.SURNAME));
            } else if (claims.hasClaim(JWTConstants.NAME)) {
                newUser.setFullName(claims.getStringClaimValue(JWTConstants.NAME));
            }
            if (claims.hasClaim(JWTConstants.EMAIL)) {
                newUser.setEmail(claims.getStringClaimValue(JWTConstants.EMAIL));
            }
            newUser.setAdmin(false);

            storage.createUser(newUser);
            return newUser;
        } catch (MalformedClaimException e) {
            log.error("Invalid claims in JWT: {}", e.getMessage());
            throw ExceptionFactory.unauthorizedException();
        }
    }

    @Override
    public UserBean getCurrentUser() {
        return get(securityContext.getCurrentUser());
    }
}