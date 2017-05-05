package be.ehb.facades;

import be.ehb.entities.users.UserBean;
import be.ehb.model.requests.JWTParseRequest;
import be.ehb.model.requests.NewUserRequest;
import be.ehb.model.responses.TokenClaimsResponse;
import be.ehb.model.responses.UserResponse;
import org.jose4j.jwt.JwtClaims;

import java.util.List;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public interface IUserFacade {

    UserBean get(String userId);

    List<UserResponse> listUsers();

    TokenClaimsResponse parseJWT(JWTParseRequest jwt);

    void initNewUser(JwtClaims claims);

    UserResponse getCurrentUser();

    UserResponse createUser(NewUserRequest request);

}