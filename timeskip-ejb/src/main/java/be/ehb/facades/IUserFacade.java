package be.ehb.facades;

import be.ehb.entities.identity.UserBean;
import be.ehb.model.requests.JWTParseRequest;
import be.ehb.model.responses.TokenClaimsResponse;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public interface IUserFacade {

    UserBean get(String userId);

    TokenClaimsResponse parseJWT(JWTParseRequest jwt);

}