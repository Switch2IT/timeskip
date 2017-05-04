package be.ehb.facades;

import be.ehb.entities.identity.UserBean;
import be.ehb.model.requests.JWTParseRequest;
import be.ehb.model.responses.TokenClaimsResponse;
import be.ehb.model.users.UserDTO;
import org.jose4j.jwt.JwtClaims;

import java.util.List;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public interface IUserFacade {

    UserBean get(String userId);

    List<UserDTO> listUsers();

    TokenClaimsResponse parseJWT(JWTParseRequest jwt);

    UserBean initNewUser(JwtClaims claims);

    UserDTO getCurrentUser();

}