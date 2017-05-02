package be.ehb.rest.resources;

import be.ehb.facades.IProjectFacade;
import be.ehb.factories.ExceptionFactory;
import be.ehb.model.activities.ActivityDTO;
import be.ehb.model.projects.ProjectDTO;
import be.ehb.model.responses.ErrorResponse;
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
import java.util.List;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
@Api(value = "/projects", description = "projects-related endpoints")
@Path("/projects")
@ApplicationScoped
public class ProjectsResource {

    @Inject
    private IProjectFacade projectFacade;
    @Inject
    private ISecurityContext securityContext;

    @ApiOperation(value = "List projects",
            notes = "Return a list of projects")
    @ApiResponses({
            @ApiResponse(code = 200, responseContainer = "List", response = ProjectDTO.class, message = "Projects list"),
            @ApiResponse(code = 400, response = ErrorResponse.class, message = "Error occurred")
    })
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<ProjectDTO> listProjects() {
        return projectFacade.listProjects();
    }

    @ApiOperation(value = "Get project",
            notes = "Get a project for a given ID")
    @ApiResponses({
            @ApiResponse(code = 200, response = ProjectDTO.class, message = "Project"),
            @ApiResponse(code = 400, response = ErrorResponse.class, message = "Error occurred")
    })
    @GET
    @Path("/{projectId}")
    @Produces(MediaType.APPLICATION_JSON)
    public ProjectDTO getProject(@PathParam("projectId") String projectId) {
        Preconditions.checkArgument(StringUtils.isNotEmpty(projectId), "Project ID must be provided");
        if (!securityContext.hasPermission(PermissionType.PROJECT_VIEW, projectId)) {
            throw ExceptionFactory.unauthorizedException(projectId);
        }
        return projectFacade.getProject(projectId);
    }

    @ApiOperation(value = "Create project",
            notes = "Create a project. A name must be provided")
    @ApiResponses({
            @ApiResponse(code = 200, response = ProjectDTO.class, message = "Project"),
            @ApiResponse(code = 400, response = ErrorResponse.class, message = "Error occurred")
    })
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ProjectDTO createProject(@ApiParam ProjectDTO project) {
        Preconditions.checkNotNull(project, "Request body must be provided");
        Preconditions.checkArgument(StringUtils.isNotEmpty(project.getName()), "Project name must be provided");
        Preconditions.checkArgument(project.getOrganizations() != null && !project.getOrganizations().isEmpty(), "At least one organiztion must be provided");
        project.getOrganizations().forEach(org -> {
            if (!securityContext.hasPermission(PermissionType.ORG_EDIT, org.getId()))
                throw ExceptionFactory.unauthorizedException(org.getId());
        });
        return projectFacade.createProject(project);
    }

    @ApiOperation(value = "Update project",
            notes = "Update a project. ID must be provided in the request body")
    @ApiResponses({
            @ApiResponse(code = 200, response = ProjectDTO.class, message = "Updated"),
            @ApiResponse(code = 400, response = ErrorResponse.class, message = "Error occurred")
    })
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ProjectDTO updateProject(@ApiParam ProjectDTO project) {
        Preconditions.checkNotNull(project, "Request body must be provided");
        Preconditions.checkArgument(StringUtils.isNotEmpty(project.getId()), "Project ID must be provided");
        if (!securityContext.hasPermission(PermissionType.PROJECT_EDIT, project.getId())) {
            throw ExceptionFactory.unauthorizedException(project.getId());
        }
        Preconditions.checkArgument(StringUtils.isNotEmpty(project.getName()), "Project name must be provided");
        Preconditions.checkArgument(project.getOrganizations() != null && !project.getOrganizations().isEmpty(), "At least one organiztion must be provided");
        project.getOrganizations().forEach(org -> {
            if (!securityContext.hasPermission(PermissionType.ORG_EDIT, org.getId()))
                throw ExceptionFactory.unauthorizedException(org.getId());
        });
        return projectFacade.updateProject(project);
    }

    @ApiOperation(value = "Patch project",
            notes = "Patch a project.")
    @ApiResponses({
            @ApiResponse(code = 200, response = ProjectDTO.class, message = "Updated"),
            @ApiResponse(code = 400, response = ErrorResponse.class, message = "Error occurred")
    })
    @PATCH
    @Path("/{projectId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ProjectDTO patchProject(@PathParam("projectId") String projectId, @ApiParam ProjectDTO project) {
        Preconditions.checkArgument(StringUtils.isNotEmpty(projectId), "Project ID must be provided");
        if (!securityContext.hasPermission(PermissionType.PROJECT_EDIT, projectId)) {
            throw ExceptionFactory.unauthorizedException(projectId);
        }
        Preconditions.checkNotNull(project,"Request body must not be empty");
        project.setId(projectId);
        return projectFacade.updateProject(project);
    }


    @ApiOperation(value = "Delete project",
            notes = "Delete a project.")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Succesful, no content"),
            @ApiResponse(code = 400, response = ErrorResponse.class, message = "Error occurred")
    })
    @DELETE
    @Path("/{projectId}")
    public void deleteProject(@PathParam("projectId") String projectId) {
        Preconditions.checkArgument(StringUtils.isNotEmpty(projectId));
        if (!securityContext.hasPermission(PermissionType.PROJECT_ADMIN, projectId)) {
            throw ExceptionFactory.unauthorizedException(projectId);
        }
        projectFacade.deleteProject(projectId);
    }

    @ApiOperation(value = "List project activities",
            notes = "Return a list of activities for project")
    @ApiResponses({
            @ApiResponse(code = 200, responseContainer = "List", response = ActivityDTO.class, message = "Activity list"),
            @ApiResponse(code = 400, response = ErrorResponse.class, message = "Error occurred")
    })
    @GET
    @Path("/{projectId}/activities/")
    @Produces(MediaType.APPLICATION_JSON)
    public List<ActivityDTO> listProjectActivities(@PathParam("projectId") String projectId) {
        Preconditions.checkArgument(StringUtils.isNotEmpty(projectId), "Project ID must be provided");
        if (!securityContext.hasPermission(PermissionType.PROJECT_VIEW, projectId)) {
            throw ExceptionFactory.unauthorizedException(projectId);
        }
        return projectFacade.listProjectActivities(projectId);
    }

    @ApiOperation(value = "Get activity",
            notes = "Get an activity for a given ID")
    @ApiResponses({
            @ApiResponse(code = 200, response = ActivityDTO.class, message = "Activity"),
            @ApiResponse(code = 400, response = ErrorResponse.class, message = "Error occurred")
    })
    @GET
    @Path("/{projectId}/activities/{activityId}")
    @Produces(MediaType.APPLICATION_JSON)
    public ActivityDTO getActivity(@PathParam("projectId") String projectId, @PathParam("activityId") String activityId) {
        Preconditions.checkArgument(StringUtils.isNotEmpty(projectId), "Project ID must be provided");
        Preconditions.checkArgument(StringUtils.isNotEmpty(activityId), "Activity ID must be provided");
        if (!securityContext.hasPermission(PermissionType.PROJECT_VIEW, projectId)) {
            throw ExceptionFactory.unauthorizedException(projectId);
        } else if (!securityContext.hasPermission(PermissionType.ACTIVITY_VIEW, activityId)) {
            throw ExceptionFactory.unauthorizedException(activityId);
        }
        return projectFacade.getActivity(projectId, activityId);
    }

    @ApiOperation(value = "Create activity",
            notes = "Create an activity. A description must be provided")
    @ApiResponses({
            @ApiResponse(code = 200, response = ActivityDTO.class, message = "Project"),
            @ApiResponse(code = 400, response = ErrorResponse.class, message = "Error occurred")
    })
    @POST
    @Path("/{projectId}/activities/")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ActivityDTO createActivity(@PathParam("projectId") String projectID, @ApiParam ActivityDTO activity) {
        Preconditions.checkArgument(StringUtils.isNotEmpty(projectID), "Project ID must be provided");
        if (!securityContext.hasPermission(PermissionType.PROJECT_EDIT, projectID)) {
            throw ExceptionFactory.unauthorizedException(projectID);
        }
        Preconditions.checkNotNull(activity, "Request body must not be empty");
        Preconditions.checkArgument(StringUtils.isNotEmpty(activity.getDescription()), "Activity description must be provided");
        return projectFacade.createActivity(projectID, activity);
    }

    @ApiOperation(value = "Update activity",
            notes = "Update an activity. ID must be provided in the request body")
    @ApiResponses({
            @ApiResponse(code = 200, response = ActivityDTO.class, message = "Updated"),
            @ApiResponse(code = 400, response = ErrorResponse.class, message = "Error occurred")
    })
    @PUT
    @Path("/{projectId}/activities")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ActivityDTO updateActivity(@PathParam("projectId") String projectId, @ApiParam ActivityDTO activity) {
        return null;
    }

    @ApiOperation(value = "Patch activity",
            notes = "Patch an activity.")
    @ApiResponses({
            @ApiResponse(code = 200, response = ActivityDTO.class, message = "Updated"),
            @ApiResponse(code = 400, response = ErrorResponse.class, message = "Error occurred")
    })
    @PATCH
    @Path("/{projectId}/activities/{activityId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ActivityDTO patchActivity(@PathParam("projectId") String projectId, @PathParam("activityId") String activityId, @ApiParam ActivityDTO activity) {
        return null;
    }

    @ApiOperation(value = "Delete activity",
            notes = "Delete an activity.")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Succesful, no content"),
            @ApiResponse(code = 400, response = ErrorResponse.class, message = "Error occurred")
    })
    @DELETE
    @Path("/{projectId}/activities/{activityId}")
    public void deleteActivity(@PathParam("projectId") String projectId, @PathParam("activityId") String activityId) {

    }
}