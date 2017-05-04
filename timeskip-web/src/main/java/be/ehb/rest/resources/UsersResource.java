package be.ehb.rest.resources;

import be.ehb.facades.IUserFacade;
import be.ehb.model.requests.JWTParseRequest;
import be.ehb.model.responses.ErrorResponse;
import be.ehb.model.responses.TokenClaimsResponse;
import be.ehb.model.users.UserDTO;
import com.google.common.base.Preconditions;
import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
@Api(value = "/users", description = "Users-related endpoints")
@Path("/users")
@ApplicationScoped
public class UsersResource {

    private static final Logger log = LoggerFactory.getLogger(UsersResource.class);

    @Inject
    private IUserFacade userFacade;

    @ApiOperation(value = "Parse a JWT",
            notes = "Parse a JWT and return the user info from the claims")
    @ApiResponses({
            @ApiResponse(code = 200, response = TokenClaimsResponse.class, message = "User info"),
            @ApiResponse(code = 400, response = ErrorResponse.class, message = "Error occurred")
    })
    @POST
    @Path("/jwt/parse")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TokenClaimsResponse parseJwt(@ApiParam(value = "JWT") JWTParseRequest jwt) {
        //TODO - Use resource bundle for internationalization
        Preconditions.checkNotNull(jwt, "Request must be provided");
        Preconditions.checkArgument(StringUtils.isNotEmpty(jwt.getJwt()), "JWT String required");
        return userFacade.parseJWT(jwt);
    }

    @ApiOperation(value = "Get current user",
            notes = "Get the current user")
    @ApiResponses({
            @ApiResponse(code = 200, response = UserDTO.class, message = "User info"),
            @ApiResponse(code = 400, response = ErrorResponse.class, message = "Error occurred")
    })
    @GET
    @Path("/current")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public UserDTO getCurrentUser() {
        //TODO - Use resource bundle for internationalization
        return userFacade.getCurrentUser();
    }

    @ApiOperation(value = "List users",
            notes = "Retrieve a list of all users.")
    @ApiResponses({
            @ApiResponse(code = 200, responseContainer = "List", response = UserDTO.class, message = "User list"),
            @ApiResponse(code = 400, response = ErrorResponse.class, message = "Error occurred")
    })
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<UserDTO> listUsers() {
        return userFacade.listUsers();
    }
}