package be.ehb.rest.resources;

import be.ehb.facades.IManagementFacade;
import be.ehb.facades.IOrganizationFacade;
import be.ehb.facades.IUserFacade;
import be.ehb.factories.ExceptionFactory;
import be.ehb.factories.ResponseFactory;
import be.ehb.model.requests.*;
import be.ehb.model.responses.*;
import be.ehb.security.ISecurityContext;
import be.ehb.security.PermissionType;
import com.google.common.base.Preconditions;
import io.swagger.annotations.*;
import io.swagger.jaxrs.PATCH;
import org.apache.commons.lang3.StringUtils;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.Response.Status.*;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
@Api(value = "/users", description = "Users-related endpoints")
@Path("/users")
@ApplicationScoped
public class UsersResource {

    @Inject
    private IUserFacade userFacade;
    @Inject
    private IOrganizationFacade orgFacade;
    @Inject
    private IManagementFacade managementFacade;
    @Inject
    private ISecurityContext securityContext;

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
    public Response parseJwt(@ApiParam(value = "JWT") JWTParseRequest jwt) {
        //TODO - Use resource bundle for internationalization
        Preconditions.checkNotNull(jwt, "Request must be provided");
        Preconditions.checkArgument(StringUtils.isNotEmpty(jwt.getJwt()), "JWT String required");
        return ResponseFactory.buildResponse(OK, userFacade.parseJWT(jwt));
    }

    @ApiOperation(value = "Get current user",
            notes = "Get the current user")
    @ApiResponses({
            @ApiResponse(code = 200, response = UserResponse.class, message = "User info"),
            @ApiResponse(code = 400, response = ErrorResponse.class, message = "Error occurred")
    })
    @GET
    @Path("/current")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCurrentUser() {
        return ResponseFactory.buildResponse(OK, userFacade.getCurrentUser());
    }

    @ApiOperation(value = "Update current user",
            notes = "Update the current user")
    @ApiResponses({
            @ApiResponse(code = 200, response = UserResponse.class, message = "Updated"),
            @ApiResponse(code = 400, response = ErrorResponse.class, message = "Error occurred")
    })
    @PATCH
    @Path("/current")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCurrentUser(@ApiParam UpdateCurrentUserRequest request) {
        Preconditions.checkNotNull(request, "Request body must be provided");
        return ResponseFactory.buildResponse(OK, userFacade.updateCurrentUser(request));
    }

    @ApiOperation(value = "List users",
            notes = "Retrieve a list of all users.")
    @ApiResponses({
            @ApiResponse(code = 200, responseContainer = "List", response = UserResponse.class, message = "User list"),
            @ApiResponse(code = 400, response = ErrorResponse.class, message = "Error occurred")
    })
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response listUsers() {
        return ResponseFactory.buildResponse(OK, userFacade.listUsers());
    }

    @ApiOperation(value = "Get user",
            notes = "Retrieve a user for a provided ID.")
    @ApiResponses({
            @ApiResponse(code = 200, response = UserResponse.class, message = "User"),
            @ApiResponse(code = 400, response = ErrorResponse.class, message = "Error occurred")
    })
    @GET
    @Path("/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@PathParam("userId") String userId) {
        return ResponseFactory.buildResponse(OK, userFacade.get(userId));
    }

    @ApiOperation(value = "Create a user",
            notes = "Create a user")
    @ApiResponses({
            @ApiResponse(code = 201, response = UserResponse.class, message = "Created"),
            @ApiResponse(code = 400, response = ErrorResponse.class, message = "Error occurred")
    })
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createUser(@ApiParam NewUserRequest request) {
        Preconditions.checkNotNull(request, "Request body must be provided");
        Preconditions.checkArgument(StringUtils.isNotEmpty(request.getFirstName()), "\"name\" must be provided");
        Preconditions.checkArgument(StringUtils.isNotEmpty(request.getLastName()), "\"surname\" must be provided");
        Preconditions.checkArgument(StringUtils.isNotEmpty(request.getEmail()), "\"email\" must be provided");
        Preconditions.checkNotNull(request.getMemberships(), "\"memberships\" must be provided");
        Preconditions.checkArgument(!request.getMemberships().isEmpty(), "At least one membership must be provided");
        request.getMemberships().forEach(membership -> {
            Preconditions.checkArgument(StringUtils.isNotEmpty(membership.getRole()), "\"role\" must be provdided");
            Preconditions.checkNotNull(membership.getOrganizationId(), "\"organizationId\" must be provided");
        });
        if (request.getDefaultHoursPerDay() != null) {
            Preconditions.checkArgument(request.getDefaultHoursPerDay() > 0, "\"defaultHoursPerDay\" must be greater than 0");
        }
        return ResponseFactory.buildResponse(CREATED, userFacade.createUser(request));
    }

    @ApiOperation(value = "Update a user",
            notes = "Update a user")
    @ApiResponses({
            @ApiResponse(code = 201, response = UserResponse.class, message = "Updated"),
            @ApiResponse(code = 400, response = ErrorResponse.class, message = "Error occurred")
    })
    @PATCH
    @Path("/{userId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateUser(@PathParam("userId") String userId, @ApiParam UpdateUserRequest request) {
        Preconditions.checkArgument(StringUtils.isNotEmpty(userId), "User ID must be provided");
        Preconditions.checkNotNull(request, "Request body must be provided");

        return ResponseFactory.buildResponse(CREATED, userFacade.updateUser(userId, request));
    }

    @ApiOperation(value = "List user memberships",
            notes = "List user memberships")
    @ApiResponses({
            @ApiResponse(code = 200, responseContainer = "List", response = MembershipResponse.class, message = "Memberships"),
            @ApiResponse(code = 400, response = ErrorResponse.class, message = "Error occurred")
    })
    @GET
    @Path("/{userId}/memberships")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listUserMemberships(@PathParam("userId") String userId) {
        Preconditions.checkArgument(StringUtils.isNotEmpty(userId), "User ID must be provided");
        return ResponseFactory.buildResponse(OK, managementFacade.listUserMemberships(userId));
    }

    @ApiOperation(value = "Delete user membership",
            notes = "Delete a user's membership in an organization")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Deleted"),
            @ApiResponse(code = 400, response = ErrorResponse.class, message = "Error occurred")
    })
    @DELETE
    @Path("/{userId}/memberships/{organizationId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listUserMemberships(@PathParam("userId") String userId, @PathParam("organizationId") String organizationId) {
        Preconditions.checkArgument(StringUtils.isNotEmpty(userId), "User ID must be provided");
        Preconditions.checkArgument(StringUtils.isNotEmpty(organizationId), "Organization ID must be provided");
        if (!securityContext.hasPermission(PermissionType.ORG_EDIT, organizationId)) {
            throw ExceptionFactory.unauthorizedException(organizationId);
        }
        managementFacade.deleteUserMembership(userId, organizationId);
        return ResponseFactory.buildResponse(NO_CONTENT);
    }

    @ApiOperation(value = "Update/create user membership",
            notes = "Update or create a user membership")
    @ApiResponses({
            @ApiResponse(code = 201, response = MembershipResponse.class, message = "Created"),
            @ApiResponse(code = 400, response = ErrorResponse.class, message = "Error occurred")
    })
    @PUT
    @Path("/{userId}/memberships/organizations/{organizationId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createUserMembership(@PathParam("userId") String userId, @PathParam("organizationId") String organizationId, @ApiParam MembershipChangeRequest request) {
        Preconditions.checkArgument(StringUtils.isNotEmpty(userId), "User ID must be provided");
        Preconditions.checkArgument(StringUtils.isNotEmpty(organizationId), "\"organizationId\" must be provided");
        Preconditions.checkArgument(StringUtils.isNotEmpty(request.getRole()), "\"role\" must be provided");
        if (!securityContext.hasPermission(PermissionType.ORG_EDIT, organizationId)) {
            throw ExceptionFactory.unauthorizedException(organizationId);
        }
        return ResponseFactory.buildResponse(CREATED, managementFacade.updateOrCreateMembership(userId, organizationId, request.getRole()));
    }

    @ApiOperation(value = "Update current user worklogs",
            notes = "Update a list of the current user's worklogs. Use this endpoint if you need to confirm many worklogs at the same time")
    @ApiResponses({
            @ApiResponse(code = 200, responseContainer = "List", response = WorklogResponse.class, message = "Updated"),
            @ApiResponse(code = 400, response = ErrorResponse.class, message = "Error occurred")
    })
    @PUT
    @Path("/current/worklogs")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCurrentUserWorklogs(@ApiParam UpdateCurrentUserWorklogRequestList request) {
        Preconditions.checkNotNull(request, "Request body must be provided");
        return ResponseFactory.buildResponse(OK, orgFacade.updateCurrentUserWorklogs(request));
    }
}