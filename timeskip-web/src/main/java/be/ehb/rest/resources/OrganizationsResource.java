package be.ehb.rest.resources;

import be.ehb.facades.IOrganizationFacade;
import be.ehb.factories.ExceptionFactory;
import be.ehb.factories.ResponseFactory;
import be.ehb.i18n.Messages;
import be.ehb.model.requests.*;
import be.ehb.model.responses.*;
import be.ehb.security.ISecurityContext;
import be.ehb.security.PermissionType;
import com.google.common.base.Preconditions;
import io.swagger.jaxrs.PATCH;
import v2.io.swagger.annotations.*;
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
        return ResponseFactory.buildResponse(OK, orgFacade.listOrganizations());
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
        Preconditions.checkArgument(StringUtils.isNotEmpty(organizationId), Messages.i18n.format("emptyPathParam", "Organization ID"));
        if (!securityContext.hasPermission(PermissionType.ORG_VIEW, organizationId)) {
            throw ExceptionFactory.unauthorizedException(organizationId);
        }
        return ResponseFactory.buildResponse(OK, orgFacade.get(organizationId));
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
        Preconditions.checkNotNull(request, Messages.i18n.format("emptyRequestBody"));
        Preconditions.checkArgument(StringUtils.isNotEmpty(request.getName()), Messages.i18n.format("emptyField", "name"));
        return ResponseFactory.buildResponse(CREATED, orgFacade.createOrganization(request));
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
    public Response patchOrganization(@PathParam("organizationId") String organizationId,
                                      @ApiParam UpdateOrganizationRequest request) {
        Preconditions.checkArgument(StringUtils.isNotEmpty(organizationId), Messages.i18n.format("emptyPathParam", "Organization ID"));
        if (!securityContext.hasPermission(PermissionType.ORG_EDIT, organizationId)) {
            throw ExceptionFactory.unauthorizedException(organizationId);
        }
        Preconditions.checkNotNull(request, Messages.i18n.format("emptyRequestBody"));
        return ResponseFactory.buildResponse(OK, orgFacade.updateOrganization(organizationId, request));
    }

    @ApiOperation(value = "Delete organization",
            notes = "Delete an organization.")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Succesful"),
            @ApiResponse(code = 400, response = ErrorResponse.class, message = "Error occurred")
    })
    @DELETE
    @Path("/{organizationId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteOrganization(@PathParam("organizationId") String organizationId) {
        Preconditions.checkArgument(StringUtils.isNotEmpty(organizationId), Messages.i18n.format("emptyPathParam", "Organization ID"));
        if (!securityContext.hasPermission(PermissionType.ORG_ADMIN, organizationId)) {
            throw ExceptionFactory.unauthorizedException(organizationId);
        }
        orgFacade.deleteOrganization(organizationId);
        return ResponseFactory.buildResponse(NO_CONTENT);
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
        Preconditions.checkArgument(StringUtils.isNotEmpty(organizationId), Messages.i18n.format("emptyPathParam", "Organization ID"));
        if (!securityContext.hasPermission(PermissionType.PROJECT_VIEW, organizationId)) {
            throw ExceptionFactory.unauthorizedException(organizationId);
        }
        return ResponseFactory.buildResponse(OK, orgFacade.listProjects(organizationId));
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
    public Response getProject(@PathParam("organizationId") String organizationId,
                               @PathParam("projectId") Long projectId) {
        Preconditions.checkArgument(StringUtils.isNotEmpty(organizationId), Messages.i18n.format("emptyPathParam", "Organization ID"));
        Preconditions.checkNotNull(projectId, Messages.i18n.format("emptyPathParam", "Project ID"));
        if (!securityContext.hasPermission(PermissionType.PROJECT_VIEW, organizationId)) {
            throw ExceptionFactory.unauthorizedException(organizationId);
        }
        return ResponseFactory.buildResponse(OK, orgFacade.getProject(organizationId, projectId));
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
    public Response createProject(@PathParam("organizationId") String organizationId,
                                  @ApiParam NewProjectRequest request) {
        Preconditions.checkArgument(StringUtils.isNotEmpty(organizationId), Messages.i18n.format("emptyPathParam", "Organization ID"));
        Preconditions.checkNotNull(request, Messages.i18n.format("emptyRequestBody"));
        Preconditions.checkArgument(StringUtils.isNotEmpty(request.getName()), Messages.i18n.format("emptyField", "name"));
        if (!securityContext.hasPermission(PermissionType.PROJECT_ADMIN, organizationId)) {
            throw ExceptionFactory.unauthorizedException(organizationId);
        }
        return ResponseFactory.buildResponse(CREATED, orgFacade.createProject(organizationId, request));
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
    public Response patchProject(@PathParam("organizationId") String organizationId,
                                 @PathParam("projectId") Long projectId,
                                 @ApiParam UpdateProjectRequest request) {
        Preconditions.checkArgument(StringUtils.isNotEmpty(organizationId), Messages.i18n.format("emptyPathParam", "Organization ID"));
        Preconditions.checkNotNull(projectId, Messages.i18n.format("emptyPathParam", "Project ID"));
        if (!securityContext.hasPermission(PermissionType.PROJECT_EDIT, organizationId)) {
            throw ExceptionFactory.unauthorizedException(organizationId);
        }
        Preconditions.checkNotNull(request, Messages.i18n.format("emptyRequestBody"));
        return ResponseFactory.buildResponse(OK, orgFacade.updateProject(organizationId, projectId, request));
    }


    @ApiOperation(value = "Delete project",
            notes = "Delete a project.")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Succesful, no content"),
            @ApiResponse(code = 400, response = ErrorResponse.class, message = "Error occurred")
    })
    @DELETE
    @Path("/{organizationId}/projects/{projectId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteProject(@PathParam("organizationId") String organizationId,
                                  @PathParam("projectId") Long projectId) {
        Preconditions.checkArgument(StringUtils.isNotEmpty(organizationId), Messages.i18n.format("emptyPathParam", "Organization ID"));
        Preconditions.checkNotNull(projectId, Messages.i18n.format("emptyPathParam", "Project ID"));
        if (!securityContext.hasPermission(PermissionType.PROJECT_ADMIN, organizationId)) {
            throw ExceptionFactory.unauthorizedException(organizationId);
        }
        orgFacade.deleteProject(organizationId, projectId);
        return ResponseFactory.buildResponse(NO_CONTENT);
    }

    @ApiOperation(value = "Assign user to project",
            notes = "Assign a user to a project.")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Succesful, no content"),
            @ApiResponse(code = 400, response = ErrorResponse.class, message = "Error occurred")
    })
    @POST
    @Path("/{organizationId}/projects/{projectId}/users/assign")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response assignUserToProject(@PathParam("organizationId") String organizationId,
                                        @PathParam("projectId") Long projectId,
                                        @ApiParam AssignmentRequest request) {
        checkProjectAssignment(organizationId, projectId, request);
        orgFacade.assignUserToProject(organizationId, projectId, request);
        return ResponseFactory.buildResponse(NO_CONTENT);
    }

    @ApiOperation(value = "Remove user from project",
            notes = "Remove a user from a project.")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Succesful, no content"),
            @ApiResponse(code = 400, response = ErrorResponse.class, message = "Error occurred")
    })
    @POST
    @Path("/{organizationId}/projects/{projectId}/users/remove")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeUserFromProject(@PathParam("organizationId") String organizationId,
                                          @PathParam("projectId") Long projectId,
                                          @ApiParam AssignmentRequest request) {
        checkProjectAssignment(organizationId, projectId, request);
        orgFacade.removeUserFromProject(organizationId, projectId, request);
        return ResponseFactory.buildResponse(NO_CONTENT);
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
    public Response listProjectActivities(@PathParam("organizationId") String organizationId,
                                          @PathParam("projectId") Long projectId) {
        Preconditions.checkArgument(StringUtils.isNotEmpty(organizationId), Messages.i18n.format("emptyPathParam", "Organization ID"));
        Preconditions.checkNotNull(projectId, Messages.i18n.format("emptyPathParam", "Project ID"));
        if (!securityContext.hasPermission(PermissionType.ACTIVITY_VIEW, organizationId)) {
            throw ExceptionFactory.unauthorizedException(projectId);
        }
        return ResponseFactory.buildResponse(OK, orgFacade.listProjectActivities(organizationId, projectId));
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
    public Response getActivity(@PathParam("organizationId") String organizationId,
                                @PathParam("projectId") Long projectId,
                                @PathParam("activityId") Long activityId) {
        Preconditions.checkArgument(StringUtils.isNotEmpty(organizationId), Messages.i18n.format("emptyPathParam", "Organization ID"));
        Preconditions.checkNotNull(projectId, Messages.i18n.format("emptyPathParam", "Project ID"));
        Preconditions.checkNotNull(activityId, Messages.i18n.format("emptyPathParam", "Activity ID"));
        if (!securityContext.hasPermission(PermissionType.ACTIVITY_VIEW, organizationId)) {
            throw ExceptionFactory.unauthorizedException(projectId);
        }
        return ResponseFactory.buildResponse(OK, orgFacade.getActivity(organizationId, projectId, activityId));
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
    public Response createActivity(@PathParam("organizationId") String organizationId,
                                   @PathParam("projectId") Long projectId,
                                   @ApiParam NewActivityRequest request) {
        Preconditions.checkArgument(StringUtils.isNotEmpty(organizationId), Messages.i18n.format("emptyPathParam", "Organization ID"));
        Preconditions.checkNotNull(projectId, Messages.i18n.format("emptyPathParam", "Project ID"));
        if (!securityContext.hasPermission(PermissionType.ACTIVITY_ADMIN, organizationId)) {
            throw ExceptionFactory.unauthorizedException(projectId);
        }
        Preconditions.checkNotNull(request, Messages.i18n.format("emptyRequestBody"));
        Preconditions.checkArgument(StringUtils.isNotEmpty(request.getDescription()), Messages.i18n.format("emptyField", "description"));
        return ResponseFactory.buildResponse(CREATED, orgFacade.createActivity(organizationId, projectId, request));
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
    public Response patchActivity(@PathParam("organizationId") String organizationId,
                                  @PathParam("projectId") Long projectId,
                                  @PathParam("activityId") Long activityId,
                                  @ApiParam UpdateActivityRequest request) {
        Preconditions.checkArgument(StringUtils.isNotEmpty(organizationId), Messages.i18n.format("emptyPathParam", "Organization ID"));
        Preconditions.checkNotNull(projectId, Messages.i18n.format("emptyPathParam", "Project ID"));
        Preconditions.checkNotNull(activityId, Messages.i18n.format("emptyPathParam", "Activity ID"));
        if (!securityContext.hasPermission(PermissionType.ACTIVITY_EDIT, organizationId)) {
            throw ExceptionFactory.unauthorizedException(projectId);
        }
        Preconditions.checkNotNull(request, Messages.i18n.format("emptyRequestBody"));
        Preconditions.checkArgument(StringUtils.isNotEmpty(request.getDescription()), Messages.i18n.format("emptyField", "description"));
        return ResponseFactory.buildResponse(OK, orgFacade.updateActivity(organizationId, projectId, activityId, request));
    }

    @ApiOperation(value = "Delete activity",
            notes = "Delete an activity.")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Deleted"),
            @ApiResponse(code = 400, response = ErrorResponse.class, message = "Error occurred")
    })
    @DELETE
    @Path("/{organizationId}/projects/{projectId}/activities/{activityId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteActivity(@PathParam("organizationId") String organizationId,
                                   @PathParam("projectId") Long projectId,
                                   @PathParam("activityId") Long activityId) {
        Preconditions.checkArgument(StringUtils.isNotEmpty(organizationId), Messages.i18n.format("emptyPathParam", "Organization ID"));
        Preconditions.checkNotNull(projectId, Messages.i18n.format("emptyPathParam", "Project ID"));
        Preconditions.checkNotNull(activityId, Messages.i18n.format("emptyPathParam", "Activity ID"));
        if (!securityContext.hasPermission(PermissionType.ACTIVITY_ADMIN, organizationId)) {
            throw ExceptionFactory.unauthorizedException(organizationId);
        }
        orgFacade.deleteActivity(organizationId, projectId, activityId);
        return ResponseFactory.buildResponse(NO_CONTENT);
    }

    @ApiOperation(value = "List activity worklogs",
            notes = "Retrieves a list of worklogs the user is permitted to see for a given activity.")
    @ApiResponses({
            @ApiResponse(code = 200, responseContainer = "List", response = WorklogResponse.class, message = "Successful"),
            @ApiResponse(code = 400, response = ErrorResponse.class, message = "Error occurred")
    })
    @GET
    @Path("/{organizationId}/projects/{projectId}/activities/{activityId}/worklogs")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listActivityWorklogs(@PathParam("organizationId") String organizationId,
                                         @PathParam("projectId") Long projectId,
                                         @PathParam("activityId") Long activityId) {
        Preconditions.checkArgument(StringUtils.isNotEmpty(organizationId), Messages.i18n.format("emptyPathParam", "Organization ID"));
        Preconditions.checkNotNull(projectId, Messages.i18n.format("emptyPathParam", "Project ID"));
        Preconditions.checkNotNull(activityId, Messages.i18n.format("emptyPathParam", "Activity ID"));
        if (!securityContext.hasPermission(PermissionType.WORKLOG_VIEW, organizationId)) {
            throw ExceptionFactory.unauthorizedException(organizationId);
        }
        return ResponseFactory.buildResponse(CREATED, orgFacade.listActivityWorklogs(organizationId, projectId, activityId));
    }

    @ApiOperation(value = "Get worklog", notes = "Retrieve a worklog for a give ID.")
    @ApiResponses({
            @ApiResponse(code = 200, response = WorklogResponse.class, message = "Succesful"),
            @ApiResponse(code = 400, response = ErrorResponse.class, message = "Error occurred")
    })
    @GET
    @Path("/{organizationId}/projects/{projectId}/activities/{activityId}/worklogs/{worklogId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getWorklog(@PathParam("organizationId") String organizationId,
                               @PathParam("projectId") Long projectId,
                               @PathParam("activityId") Long activityId,
                               @PathParam("worklogId") Long worklogId) {
        Preconditions.checkArgument(StringUtils.isNotEmpty(organizationId), Messages.i18n.format("emptyPathParam", "Organization ID"));
        Preconditions.checkNotNull(projectId, Messages.i18n.format("emptyPathParam", "Project ID"));
        Preconditions.checkNotNull(activityId, Messages.i18n.format("emptyPathParam", "Activity ID"));
        Preconditions.checkNotNull(worklogId, Messages.i18n.format("emptyPathParam", "Worklog ID"));
        if (!securityContext.hasPermission(PermissionType.WORKLOG_VIEW, organizationId)) {
            throw ExceptionFactory.unauthorizedException(organizationId);
        }
        return ResponseFactory.buildResponse(OK, orgFacade.getWorklog(organizationId, projectId, activityId, worklogId));
    }

    @ApiOperation(value = "Create worklog",
            notes = "Create a worklog entry on an activity. Day and logged minutes must be provided. If the log is unconfirmed a reminder will be sent at a pre-configured date.")
    @ApiResponses({
            @ApiResponse(code = 201, response = WorklogResponse.class, message = "Created"),
            @ApiResponse(code = 400, response = ErrorResponse.class, message = "Error occurred")
    })
    @POST
    @Path("/{organizationId}/projects/{projectId}/activities/{activityId}/worklogs")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response logWork(@PathParam("organizationId") String organizationId,
                            @PathParam("projectId") Long projectId,
                            @PathParam("activityId") Long activityId,
                            @ApiParam NewAdminWorklogRequest request) {
        Preconditions.checkArgument(StringUtils.isNotEmpty(organizationId), Messages.i18n.format("emptyPathParam", "Organization ID"));
        Preconditions.checkNotNull(projectId, Messages.i18n.format("emptyPathParam", "Project ID"));
        Preconditions.checkNotNull(activityId, Messages.i18n.format("emptyPathParam", "Activity ID"));
        if (!securityContext.hasPermission(PermissionType.WORKLOG_ADMIN, organizationId)) {
            throw ExceptionFactory.unauthorizedException(organizationId);
        }
        Preconditions.checkNotNull(request, Messages.i18n.format("emptyRequestBody"));
        Preconditions.checkArgument(StringUtils.isNotEmpty(request.getDay()), Messages.i18n.format("emptyField", "day"));
        Preconditions.checkNotNull(request.getLoggedMinutes(), Messages.i18n.format("emptyField", "loggedMinutes"));
        Preconditions.checkArgument(request.getLoggedMinutes() > 0, Messages.i18n.format("greaterThanZero", "loggedMinutes"));
        Preconditions.checkArgument(request.getLoggedMinutes() <= 24 * 60, Messages.i18n.format("mustBeLessThan", "loggedMinutes", (60 * 24)));
        return ResponseFactory.buildResponse(CREATED, orgFacade.createWorkLog(organizationId, projectId, activityId, request));
    }

    @ApiOperation(value = "Create worklog for current user",
            notes = "Create a worklog entry on an activity for the current user. Day and logged minutes must be provided. If the log is unconfirmed a reminder will be sent at a pre-configured date.")
    @ApiResponses({
            @ApiResponse(code = 201, response = WorklogResponse.class, message = "Created"),
            @ApiResponse(code = 400, response = ErrorResponse.class, message = "Error occurred")
    })
    @POST
    @Path("/{organizationId}/projects/{projectId}/activities/{activityId}/worklogs/currentuser")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response logWorkForCurrentUser(@PathParam("organizationId") String organizationId,
                                          @PathParam("projectId") Long projectId,
                                          @PathParam("activityId") Long activityId,
                                          @ApiParam NewWorklogRequest request) {
        Preconditions.checkNotNull(request, Messages.i18n.format("emptyRequestBody"));
        NewAdminWorklogRequest req = new NewAdminWorklogRequest();
        req.setConfirmed(request.getConfirmed());
        req.setDay(request.getDay());
        req.setLoggedMinutes(request.getLoggedMinutes());
        req.setUserId(securityContext.getCurrentUser());
        return logWork(organizationId, projectId, activityId, req);
    }

    @ApiOperation(value = "Update worklog",
            notes = "Update a worklog")
    @ApiResponses({
            @ApiResponse(code = 200, response = WorklogResponse.class, message = "Updated"),
            @ApiResponse(code = 400, response = ErrorResponse.class, message = "Error occurred")
    })
    @PATCH
    @Path("/{organizationId}/projects/{projectId}/activities/{activityId}/worklogs/{worklogId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateWorklog(@PathParam("organizationId") String organizationId,
                                  @PathParam("projectId") Long projectId,
                                  @PathParam("activityId") Long activityId,
                                  @PathParam("worklogId") Long worklogId,
                                  @ApiParam UpdateWorklogRequest request) {
        Preconditions.checkArgument(StringUtils.isNotEmpty(organizationId), Messages.i18n.format("emptyPathParam", "Organization ID"));
        Preconditions.checkNotNull(projectId, Messages.i18n.format("emptyPathParam", "Project ID"));
        Preconditions.checkNotNull(activityId, Messages.i18n.format("emptyPathParam", "Activity ID"));
        Preconditions.checkNotNull(worklogId, Messages.i18n.format("emptyPathParam", "Worklog ID"));
        if (!securityContext.hasPermission(PermissionType.WORKLOG_EDIT, organizationId)) {
            throw ExceptionFactory.unauthorizedException(organizationId);
        }
        Preconditions.checkNotNull(request, Messages.i18n.format("emptyRequestBody"));
        if (request.getLoggedMinutes() != null) {
            Preconditions.checkArgument(request.getLoggedMinutes() > 0, Messages.i18n.format("greaterThanZero", "loggedMinutes"));
            Preconditions.checkArgument(request.getLoggedMinutes() <= 24 * 60, Messages.i18n.format("mustBeLessThan", "loggedMinutes", (60 * 24)));
        }
        return ResponseFactory.buildResponse(OK, orgFacade.updateWorklog(organizationId, projectId, activityId, request));
    }

    @ApiOperation(value = "Delete worklog",
            notes = "Delete a worklog.")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Deleted"),
            @ApiResponse(code = 400, response = ErrorResponse.class, message = "Error occurred")
    })
    @DELETE
    @Path("/{organizationId}/projects/{projectId}/activities/{activityId}/worklogs/{worklogId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteWorklog(@PathParam("organizationId") String organizationId,
                                  @PathParam("projectId") Long projectId,
                                  @PathParam("activityId") Long activityId,
                                  @PathParam("worklogId") Long worklogId) {
        Preconditions.checkArgument(StringUtils.isNotEmpty(organizationId), Messages.i18n.format("emptyPathParam", "Organization ID"));
        Preconditions.checkNotNull(projectId, Messages.i18n.format("emptyPathParam", "Project ID"));
        Preconditions.checkNotNull(activityId, Messages.i18n.format("emptyPathParam", "Activity ID"));
        Preconditions.checkNotNull(worklogId, Messages.i18n.format("emptyPathParam", "Worklog ID"));
        if (!securityContext.hasPermission(PermissionType.WORKLOG_ADMIN, organizationId)) {
            throw ExceptionFactory.unauthorizedException();
        }
        orgFacade.deleteWorklog(organizationId, projectId, activityId, worklogId);
        return ResponseFactory.buildResponse(NO_CONTENT);
    }

    private void checkProjectAssignment(String organizationId, Long projectId, AssignmentRequest request) {
        Preconditions.checkArgument(StringUtils.isNotEmpty(organizationId), Messages.i18n.format("emptyPathParam", "Organization ID"));
        Preconditions.checkNotNull(projectId, Messages.i18n.format("emptyPathParam", "Project ID"));
        Preconditions.checkNotNull(request, Messages.i18n.format("emptyRequestBody"));
        Preconditions.checkArgument(StringUtils.isNotEmpty(request.getUserId()), Messages.i18n.format("emptyField", "userId"));
        if (!securityContext.hasPermission(PermissionType.PROJECT_EDIT, organizationId)) {
            throw ExceptionFactory.unauthorizedException(organizationId);
        }
    }
}