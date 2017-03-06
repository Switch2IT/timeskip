package be.ehb.facades;

import be.ehb.model.requests.JWTParseRequest;
import be.ehb.model.responses.TokenClaimsResponse;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public interface IUserFacade {

    TokenClaimsResponse parseJWT(JWTParseRequest jwt);

}