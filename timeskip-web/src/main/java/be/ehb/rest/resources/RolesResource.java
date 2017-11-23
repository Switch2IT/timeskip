package be.ehb.rest.resources;

import be.ehb.facades.IRoleFacade;
import be.ehb.factories.ExceptionFactory;
import be.ehb.factories.ResponseFactory;
import be.ehb.i18n.Messages;
import be.ehb.model.requests.NewRoleRequest;
import be.ehb.model.requests.UpdateRoleRequest;
import be.ehb.model.responses.ErrorResponse;
import be.ehb.model.responses.RoleResponse;
import be.ehb.security.ISecurityContext;
import com.google.common.base.Preconditions;
import v2.io.swagger.annotations.*;
import io.swagger.jaxrs.PATCH;
import org.apache.commons.collections.CollectionUtils;
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
@Api(value = "/roles", description = "Roles-related endpoints")
@Path("/roles")
@ApplicationScoped
public class RolesResource {

    @Inject
    private IRoleFacade roleFacade;
    @Inject
    private ISecurityContext securityContext;

    @ApiOperation(value = "List roles",
            notes = "List all available roles")
    @ApiResponses({
            @ApiResponse(code = 200, responseContainer = "List", response = RoleResponse.class, message = "Roles"),
            @ApiResponse(code = 400, response = ErrorResponse.class, message = "Error occurred")
    })
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response listRoles() {
        return ResponseFactory.buildResponse(OK, roleFacade.listRoles());
    }

    @ApiOperation(value = "Get role",
            notes = "Retrieve a role for a given ID.")
    @ApiResponses({
            @ApiResponse(code = 200, response = RoleResponse.class, message = "Role"),
            @ApiResponse(code = 400, response = ErrorResponse.class, message = "Error occurred")
    })
    @GET
    @Path("/{roleId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@PathParam("roleId") String roleId) {
        Preconditions.checkArgument(StringUtils.isNotEmpty(roleId), Messages.i18n.format("emptyPathParam", "Role ID"));
        return ResponseFactory.buildResponse(OK, roleFacade.get(roleId));
    }

    @ApiOperation(value = "Create role",
            notes = "Create a role. Permissions must be one of: ORG_VIEW, ORG_EDIT, ORG_ADMIN, PROJECT_VIEW, PROJECT_VIEW_ALL, PROJECT_EDIT, PROJECT_ADMIN, ACTIVITY_VIEW, ACTIVITY_EDIT, ACTIVITY_ADMIN, WORKLOG_VIEW, WORKLOG_VIEW_ALL, WORKLOG_EDIT, WORKLOG_EDIT_ALL, WORKLOG_ADMIN, WORKLOG_ADMIN_ALL.")
    @ApiResponses({
            @ApiResponse(code = 201, response = RoleResponse.class, message = "Created"),
            @ApiResponse(code = 400, response = ErrorResponse.class, message = "Error occurred")
    })
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(@ApiParam NewRoleRequest request) {
        Preconditions.checkNotNull(request, Messages.i18n.format("emptyRequestBody"));
        Preconditions.checkArgument(StringUtils.isNotEmpty(request.getName()), Messages.i18n.format("emptyField", "name"));
        Preconditions.checkArgument(CollectionUtils.isNotEmpty(request.getPermissions()), Messages.i18n.format("emptyList", "permissions"));
        if (!securityContext.isAdmin()) throw ExceptionFactory.unauthorizedException();
        return ResponseFactory.buildResponse(CREATED, roleFacade.create(request));
    }

    @ApiOperation(value = "Update role",
            notes = "Update a role with a given ID. Permissions must be one of: ORG_VIEW, ORG_EDIT, ORG_ADMIN, PROJECT_VIEW, PROJECT_VIEW_ALL, PROJECT_EDIT, PROJECT_ADMIN, ACTIVITY_VIEW, ACTIVITY_EDIT, ACTIVITY_ADMIN, WORKLOG_VIEW, WORKLOG_VIEW_ALL, WORKLOG_EDIT, WORKLOG_EDIT_ALL, WORKLOG_ADMIN, WORKLOG_ADMIN_ALL.")
    @ApiResponses({
            @ApiResponse(code = 200, response = RoleResponse.class, message = "Updated"),
            @ApiResponse(code = 400, response = ErrorResponse.class, message = "Error occurred")
    })
    @PATCH
    @Path("/{roleId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("roleId") String roleId,
                           @ApiParam UpdateRoleRequest request) {
        if (!securityContext.isAdmin()) throw ExceptionFactory.unauthorizedException();
        Preconditions.checkArgument(StringUtils.isNotEmpty(roleId), Messages.i18n.format("emptyPathParam", "Role ID"));
        Preconditions.checkNotNull(request, Messages.i18n.format("emptyRequestBody"));
        Preconditions.checkArgument(CollectionUtils.isNotEmpty(request.getPermissions()), Messages.i18n.format("emptyList", "permissions"));
        return ResponseFactory.buildResponse(OK, roleFacade.update(roleId, request));
    }

    @ApiOperation(value = "Delete role",
            notes = "Delete a role with a given ID")
    @ApiResponses({
            @ApiResponse(code = 204, response = RoleResponse.class, message = "Deleted"),
            @ApiResponse(code = 400, response = ErrorResponse.class, message = "Error occurred")
    })
    @DELETE
    @Path("/{roleId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("roleId") String roleId) {
        if (!securityContext.isAdmin()) throw ExceptionFactory.unauthorizedException();
        Preconditions.checkArgument(StringUtils.isNotEmpty(roleId), Messages.i18n.format("emptyPathParam", "Role ID"));
        roleFacade.delete(roleId);
        return ResponseFactory.buildResponse(NO_CONTENT);
    }
}