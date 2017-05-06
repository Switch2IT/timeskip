package be.ehb.rest.resources;

import be.ehb.facades.IOrganizationFacade;
import be.ehb.factories.ExceptionFactory;
import be.ehb.factories.ResponseFactory;
import be.ehb.model.requests.*;
import be.ehb.model.responses.ActivityResponse;
import be.ehb.model.responses.ErrorResponse;
import be.ehb.model.responses.OrganizationResponse;
import be.ehb.model.responses.ProjectResponse;
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

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
@Api(value = "/organizations", description = "Organizations-related endpoints")
@Path("/organizations")
@ApplicationScoped
public class OrganizationsResource {

    @Inject
    private IOrganizationFacade orgFacade;
    @Inject
    private ISecurityContext securityContext;

    @ApiOperation(value = "List organizations",
            notes = "Return a list of organizations")
    @ApiResponses({
            @ApiResponse(code = 200, responseContainer = "List", response = OrganizationResponse.class, message = "Organization list"),
            @ApiResponse(code = 400, response = ErrorResponse.class, message = "Error occurred")
    })
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response listOrganizations() {
        return ResponseFactory.buildResponse(Response.Status.OK.getStatusCode(), orgFacade.listOrganizations());
    }

    @ApiOperation(value = "Get organization",
            notes = "Get an organization for a given ID")
    @ApiResponses({
            @ApiResponse(code = 200, response = OrganizationResponse.class, message = "Organization"),
            @ApiResponse(code = 400, response = ErrorResponse.class, message = "Error occurred")
    })
    @GET
    @Path("/{organizationId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOrganization(@PathParam("organizationId") String organizationId) {
        Preconditions.checkArgument(StringUtils.isNotEmpty(organizationId), "Organization ID must be provided");
        if (!securityContext.hasPermission(PermissionType.ORG_VIEW, organizationId)) {
            throw ExceptionFactory.unauthorizedException(organizationId);
        }
        return ResponseFactory.buildResponse(Response.Status.OK.getStatusCode(), orgFacade.get(organizationId));
    }

    @ApiOperation(value = "Create organization",
            notes = "Create an organization. A name must be provided")
    @ApiResponses({
            @ApiResponse(code = 201, response = OrganizationResponse.class, message = "Created"),
            @ApiResponse(code = 400, response = ErrorResponse.class, message = "Error occurred")
    })
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createOrganization(@ApiParam NewOrganizationRequest request) {
        Preconditions.checkNotNull(request, "Organization request must be provided");
        Preconditions.checkArgument(StringUtils.isNotEmpty(request.getName()), "\"name\" must be provided");
        return ResponseFactory.buildResponse(Response.Status.CREATED.getStatusCode(), orgFacade.createOrganization(request));
    }

    @ApiOperation(value = "Update organization",
            notes = "Update an organization.")
    @ApiResponses({
            @ApiResponse(code = 200, response = OrganizationResponse.class, message = "Updated"),
            @ApiResponse(code = 400, response = ErrorResponse.class, message = "Error occurred")
    })
    @PATCH
    @Path("/{organizationId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response patchOrganization(@PathParam("organizationId") String organizationId, @ApiParam UpdateOrganizationRequest request) {
        Preconditions.checkArgument(StringUtils.isNotEmpty(organizationId), "Organization ID must be provided");
        if (!securityContext.hasPermission(PermissionType.ORG_EDIT, organizationId)) {
            throw ExceptionFactory.unauthorizedException(organizationId);
        }
        Preconditions.checkNotNull(request, "Request body must be provided");
        return ResponseFactory.buildResponse(Response.Status.OK.getStatusCode(), orgFacade.updateOrganization(organizationId, request));
    }

    @ApiOperation(value = "Delete organization",
            notes = "Delete an organization.")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Succesful"),
            @ApiResponse(code = 400, response = ErrorResponse.class, message = "Error occurred")
    })
    @DELETE
    @Path("/{organizationId}")
    public Response deleteOrganization(@PathParam("organizationId") String organizationId) {
        Preconditions.checkArgument(StringUtils.isNotEmpty(organizationId), "Organization ID must be provided");
        if (!securityContext.isAdmin()) {
            throw ExceptionFactory.unauthorizedException(organizationId);
        }
        orgFacade.deleteOrganization(organizationId);
        return ResponseFactory.buildResponse(Response.Status.NO_CONTENT.getStatusCode());
    }

    @ApiOperation(value = "List projects",
            notes = "Return a list of projects")
    @ApiResponses({
            @ApiResponse(code = 200, responseContainer = "List", response = ProjectResponse.class, message = "Projects list"),
            @ApiResponse(code = 400, response = ErrorResponse.class, message = "Error occurred")
    })
    @GET
    @Path("/{organizationId}/projects")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listProjects(@PathParam("organizationId") String organizationId) {
        Preconditions.checkArgument(StringUtils.isNotEmpty(organizationId), "Organization ID must be provided");
        if (!securityContext.hasPermission(PermissionType.PROJECT_VIEW, organizationId)) {
            throw ExceptionFactory.unauthorizedException(organizationId);
        }
        return ResponseFactory.buildResponse(Response.Status.OK.getStatusCode(), orgFacade.listProjects(organizationId));
    }

    @ApiOperation(value = "Get project",
            notes = "Get a project for a given ID")
    @ApiResponses({
            @ApiResponse(code = 200, response = ProjectResponse.class, message = "Project"),
            @ApiResponse(code = 400, response = ErrorResponse.class, message = "Error occurred")
    })
    @GET
    @Path("/{organizationId}/projects/{projectId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProject(@PathParam("organizationId") String organizationId, @PathParam("projectId") Long projectId) {
        Preconditions.checkArgument(StringUtils.isNotEmpty(organizationId), "Organization ID must be provided");
        Preconditions.checkNotNull(projectId, "Project ID must be provided");
        if (!securityContext.hasPermission(PermissionType.PROJECT_VIEW, organizationId)) {
            throw ExceptionFactory.unauthorizedException(organizationId);
        }
        return ResponseFactory.buildResponse(Response.Status.OK.getStatusCode(), orgFacade.getProject(organizationId, projectId));
    }

    @ApiOperation(value = "Create project",
            notes = "Create a project. A name must be provided")
    @ApiResponses({
            @ApiResponse(code = 201, response = ProjectResponse.class, message = "Project"),
            @ApiResponse(code = 400, response = ErrorResponse.class, message = "Error occurred")
    })
    @POST
    @Path("/{organizationId}/projects")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createProject(@PathParam("organizationId") String organizationId, @ApiParam NewProjectRequest request) {
        Preconditions.checkArgument(StringUtils.isNotEmpty(organizationId), "Organization ID must be provided");
        Preconditions.checkNotNull(request, "Request body must be provided");
        Preconditions.checkArgument(StringUtils.isNotEmpty(request.getName()), "\"name\" must be provided");
        if (!securityContext.hasPermission(PermissionType.PROJECT_ADMIN, organizationId)) {
            throw ExceptionFactory.unauthorizedException(organizationId);
        }
        return ResponseFactory.buildResponse(Response.Status.CREATED.getStatusCode(), orgFacade.createProject(organizationId, request));
    }

    @ApiOperation(value = "Update project",
            notes = "Update a project.")
    @ApiResponses({
            @ApiResponse(code = 200, response = ProjectResponse.class, message = "Updated"),
            @ApiResponse(code = 400, response = ErrorResponse.class, message = "Error occurred")
    })
    @PATCH
    @Path("/{organizationId}/projects/{projectId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response patchProject(@PathParam("organizationId") String organizationId, @PathParam("projectId") Long projectId, @ApiParam UpdateProjectRequest request) {
        Preconditions.checkArgument(StringUtils.isNotEmpty(organizationId), "Organization ID must be provided");
        Preconditions.checkNotNull(projectId, "Project ID must be provided");
        if (!securityContext.hasPermission(PermissionType.PROJECT_EDIT, organizationId)) {
            throw ExceptionFactory.unauthorizedException(organizationId);
        }
        Preconditions.checkNotNull(request, "Request body must not be empty");
        return ResponseFactory.buildResponse(Response.Status.OK.getStatusCode(), orgFacade.updateProject(organizationId, projectId, request));
    }


    @ApiOperation(value = "Delete project",
            notes = "Delete a project.")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Succesful, no content"),
            @ApiResponse(code = 400, response = ErrorResponse.class, message = "Error occurred")
    })
    @DELETE
    @Path("/{organizationId}/projects/{projectId}")
    public Response deleteProject(@PathParam("organizationId") String organizationId, @PathParam("projectId") Long projectId) {
        Preconditions.checkArgument(StringUtils.isNotEmpty(organizationId), "Organization ID must be provided");
        Preconditions.checkNotNull(projectId, "Project ID must be provided");
        if (!securityContext.hasPermission(PermissionType.PROJECT_ADMIN, organizationId)) {
            throw ExceptionFactory.unauthorizedException(organizationId);
        }
        orgFacade.deleteProject(organizationId, projectId);
        return ResponseFactory.buildResponse(Response.Status.NO_CONTENT.getStatusCode());
    }

    @ApiOperation(value = "List project activities",
            notes = "Return a list of activities for project")
    @ApiResponses({
            @ApiResponse(code = 200, responseContainer = "List", response = ActivityResponse.class, message = "Activity list"),
            @ApiResponse(code = 400, response = ErrorResponse.class, message = "Error occurred")
    })
    @GET
    @Path("/{organizationId}/projects/{projectId}/activities/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listProjectActivities(@PathParam("organizationId") String organizationId, @PathParam("projectId") Long projectId) {
        Preconditions.checkArgument(StringUtils.isNotEmpty(organizationId), "Organization ID must be provided");
        Preconditions.checkNotNull(projectId, "Project ID must be provided");
        if (!securityContext.hasPermission(PermissionType.ACTIVITY_VIEW, organizationId)) {
            throw ExceptionFactory.unauthorizedException(projectId);
        }
        return ResponseFactory.buildResponse(Response.Status.OK.getStatusCode(), orgFacade.listProjectActivities(organizationId, projectId));
    }

    @ApiOperation(value = "Get activity",
            notes = "Get an activity for a given ID")
    @ApiResponses({
            @ApiResponse(code = 200, response = ActivityResponse.class, message = "Activity"),
            @ApiResponse(code = 400, response = ErrorResponse.class, message = "Error occurred")
    })
    @GET
    @Path("/{organizationId}/projects/{projectId}/activities/{activityId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getActivity(@PathParam("organizationId") String organizationId, @PathParam("projectId") Long projectId, @PathParam("activityId") Long activityId) {
        Preconditions.checkArgument(StringUtils.isNotEmpty(organizationId), "Organization ID must be provided");
        Preconditions.checkNotNull(projectId, "Project ID must be provided");
        Preconditions.checkNotNull(activityId, "Activity ID must be provided");
        if (!securityContext.hasPermission(PermissionType.ACTIVITY_VIEW, organizationId)) {
            throw ExceptionFactory.unauthorizedException(projectId);
        }
        return ResponseFactory.buildResponse(Response.Status.OK.getStatusCode(), orgFacade.getActivity(organizationId, projectId, activityId));
    }

    @ApiOperation(value = "Create activity",
            notes = "Create an activity. A description must be provided")
    @ApiResponses({
            @ApiResponse(code = 201, response = ActivityResponse.class, message = "Project"),
            @ApiResponse(code = 400, response = ErrorResponse.class, message = "Error occurred")
    })
    @POST
    @Path("/{organizationId}/projects/{projectId}/activities/")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createActivity(@PathParam("organizationId") String organizationId, @PathParam("projectId") Long projectId, @ApiParam NewActivityRequest request) {
        Preconditions.checkArgument(StringUtils.isNotEmpty(organizationId), "Organization ID must be provided");
        Preconditions.checkNotNull(projectId, "Project ID must be provided");
        if (!securityContext.hasPermission(PermissionType.ACTIVITY_ADMIN, organizationId)) {
            throw ExceptionFactory.unauthorizedException(projectId);
        }
        Preconditions.checkNotNull(request, "Request body must not be empty");
        Preconditions.checkArgument(StringUtils.isNotEmpty(request.getDescription()), "\"description\" must be provided");
        return ResponseFactory.buildResponse(Response.Status.CREATED.getStatusCode(), orgFacade.createActivity(organizationId, projectId, request));
    }

    @ApiOperation(value = "Patch activity",
            notes = "Patch an activity.")
    @ApiResponses({
            @ApiResponse(code = 200, response = ActivityResponse.class, message = "Updated"),
            @ApiResponse(code = 400, response = ErrorResponse.class, message = "Error occurred")
    })
    @PATCH
    @Path("/{organizationId}/projects/{projectId}/activities/{activityId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response patchActivity(@PathParam("organizationId") String organizationId, @PathParam("projectId") Long projectId, @PathParam("activityId") Long activityId, @ApiParam UpdateActivityRequest request) {
        Preconditions.checkArgument(StringUtils.isNotEmpty(organizationId), "Organization ID must be provided");
        Preconditions.checkNotNull(projectId, "Project ID must be provided");
        Preconditions.checkNotNull(activityId, "Activity ID must be provided");
        if (!securityContext.hasPermission(PermissionType.ACTIVITY_EDIT, organizationId)) {
            throw ExceptionFactory.unauthorizedException(projectId);
        }
        Preconditions.checkNotNull(request, "Request body must not be empty");
        Preconditions.checkArgument(StringUtils.isNotEmpty(request.getDescription()), "\"description\" must be provided");
        return ResponseFactory.buildResponse(Response.Status.OK.getStatusCode(), orgFacade.updateActivity(organizationId, projectId, activityId, request));
    }

    @ApiOperation(value = "Delete activity",
            notes = "Delete an activity.")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Succesful, no content"),
            @ApiResponse(code = 400, response = ErrorResponse.class, message = "Error occurred")
    })
    @DELETE
    @Path("/{organizationId}/projects/{projectId}/activities/{activityId}")
    public Response deleteActivity(@PathParam("organizationId") String organizationId, @PathParam("projectId") Long projectId, @PathParam("activityId") Long activityId) {
        Preconditions.checkArgument(StringUtils.isNotEmpty(organizationId), "Organization ID must be provided");
        Preconditions.checkNotNull(projectId, "Project ID must be provided");
        Preconditions.checkNotNull(activityId, "Activity ID must be provided");
        if (!securityContext.hasPermission(PermissionType.ACTIVITY_ADMIN, organizationId)) {
            throw ExceptionFactory.unauthorizedException(organizationId);
        }
        orgFacade.deleteActivity(organizationId, projectId, activityId);
        return ResponseFactory.buildResponse(Response.Status.NO_CONTENT.getStatusCode());
    }

    @ApiOperation(value = "Log work", notes = "Create a worklog entry on an activity. Day and logged minutes must be provided. If the log is unconfirmed a reminder will be sent at a pre-configured date.")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 400, response = ErrorResponse.class, message = "Error occurred")
    })
    @POST
    @Path("/{organizationId}/projects/{projectId}/activities/{activityId}/worklogs")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response logWork(@PathParam("organizationId") String organizationId, @PathParam("projectId") Long projectId, @PathParam("activityId") Long activityId, @ApiParam NewWorklogRequest request) {
        Preconditions.checkArgument(StringUtils.isNotEmpty(organizationId), "Organization ID must be provided");
        Preconditions.checkNotNull(projectId, "Project ID must be provided");
        Preconditions.checkNotNull(activityId, "Activity ID must be provided");
        if (!securityContext.hasPermission(PermissionType.WORKLOG_ADMIN, organizationId)) {
            throw ExceptionFactory.unauthorizedException(organizationId);
        }
        Preconditions.checkNotNull(request, "Request body must be provided");
        Preconditions.checkArgument(StringUtils.isNotEmpty(request.getDay()), "\"day\" must be provided");
        Preconditions.checkArgument(request.getLoggedMinutes() != null, "\"loggedMinutes\" must be provided");
        Preconditions.checkArgument(request.getLoggedMinutes() > 0, "\"loggedMinutes\" must be greater than 0");
        return ResponseFactory.buildResponse(Response.Status.CREATED.getStatusCode(), orgFacade.createWorkLog(organizationId, projectId, activityId, request));
    }
}