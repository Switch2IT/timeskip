package be.ehb.facades;

import be.ehb.entities.users.UserBean;
import be.ehb.entities.users.UsersWorkLoadActivityBO;
import be.ehb.model.requests.JWTParseRequest;
import be.ehb.model.requests.NewUserRequest;
import be.ehb.model.requests.UpdateCurrentUserRequest;
import be.ehb.model.requests.UpdateUserRequest;
import be.ehb.model.responses.TokenClaimsResponse;
import be.ehb.model.responses.UserResponse;
import org.jose4j.jwt.JwtClaims;

import java.util.Date;
import java.util.List;

/**
 * @author Guillaume Vandecasteele / Patrick Van den Bussche
 * @since 2017
 */
public interface IUserFacade {

    UserBean get(String userId);

    UserResponse getUser(String userId);

    List<UserResponse> listUsers(String organizationId, String roleId, String userId, String firstName, String lastName, String email);

    TokenClaimsResponse parseJWT(JWTParseRequest jwt);

    void initNewUser(JwtClaims claims);

    UserResponse getCurrentUser();

    UserResponse updateCurrentUser(UpdateCurrentUserRequest request);

    UserResponse updateUser(String userId, UpdateUserRequest request);

    UserResponse createUser(NewUserRequest request);

    List<UsersWorkLoadActivityBO> listUsersWorkloadActivity(Date day);

}