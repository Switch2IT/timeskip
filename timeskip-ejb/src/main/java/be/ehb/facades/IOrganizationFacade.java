package be.ehb.facades;

import be.ehb.entities.projects.WorklogBean;
import be.ehb.model.requests.*;
import be.ehb.model.responses.ActivityResponse;
import be.ehb.model.responses.OrganizationResponse;
import be.ehb.model.responses.ProjectResponse;
import be.ehb.model.responses.WorklogResponse;

import java.util.List;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public interface IOrganizationFacade {

    /**
     * Retrieve a list of all organizations
     *
     * @return List of organizations
     */
    List<OrganizationResponse> listOrganizations();

    /**
     * Get an organization by id
     *
     * @param organizationId the organization ID
     * @return Organization
     */
    OrganizationResponse get(String organizationId);

    /**
     * Get or create an organization
     *
     * @param request the organization
     * @return Organization
     */
    OrganizationResponse createOrganization(NewOrganizationRequest request);

    /**
     * Update an organization
     *
     * @param organizationId the organization id
     * @param request        the update request
     * @return Organization
     */
    OrganizationResponse updateOrganization(String organizationId, UpdateOrganizationRequest request);

    /**
     * Delete an organization
     *
     * @param organizationId the organization id
     */
    void deleteOrganization(String organizationId);

    /**
     * List organization projects
     *
     * @param organizationId the organization id
     * @return the organization projects
     */
    List<ProjectResponse> listProjects(String organizationId);

    /**
     * Get project for given ID
     *
     * @param organizationId the organization id
     * @param projectId      the project id
     * @return Project
     */
    ProjectResponse getProject(String organizationId, Long projectId);

    /**
     * Create a project
     *
     * @param organizationId the organization id
     * @param request        the project
     * @return Project
     */
    ProjectResponse createProject(String organizationId, NewProjectRequest request);

    /**
     * Update a project
     *
     * @param organizationId the organization id
     * @param request        the update request
     * @return Project
     */
    ProjectResponse updateProject(String organizationId, Long projectId, UpdateProjectRequest request);

    /**
     * Delete a project
     *
     * @param organizationId the organization id
     * @param projectId      the project id
     */
    void deleteProject(String organizationId, Long projectId);

    /**
     * List activities in project
     *
     * @param organizationId the organization id
     * @param projectId      the project id
     * @return Activity
     */
    List<ActivityResponse> listProjectActivities(String organizationId, Long projectId);

    /**
     * Get activity for given ID
     *
     * @param organizationId the organization id
     * @param projectId      the project id
     * @param activityId     the activity id
     * @return Activity
     */
    ActivityResponse getActivity(String organizationId, Long projectId, Long activityId);

    /**
     * Create an activity in a project
     *
     * @param organizationId the organization id
     * @param projectID      the project id
     * @param request        the activity
     * @return Activity
     */
    ActivityResponse createActivity(String organizationId, Long projectID, NewActivityRequest request);

    /**
     * Update an activity
     *
     * @param organizationId the organization id
     * @param projectId      the project id
     * @param request        the update request
     * @return the updated activity
     */
    ActivityResponse updateActivity(String organizationId, Long projectId, Long activityId, UpdateActivityRequest request);

    /**
     * Delete an activity
     *
     * @param organizationId the organization id
     * @param projectId      the project id
     * @param activityId     the activity id
     */
    void deleteActivity(String organizationId, Long projectId, Long activityId);

    /**
     * List worklogs for an activity
     *
     * @param organizationId the organization id
     * @param projectId      the project id
     * @param activityId     the activity id
     */
    List<WorklogResponse> listActivityWorklogs(String organizationId, Long projectId, Long activityId);

    /**
     * Get a worklog
     *
     * @param organizationId the organization id
     * @param projectId      the project id
     * @param activityId     the activity id
     * @param worklogId      the worklog id
     * @return the worklog
     */
    WorklogResponse getWorklog(String organizationId, Long projectId, Long activityId, Long worklogId);

    /**
     * Log work on an activity
     *
     * @param organizationId the organization id
     * @param projectId      the project id
     * @param activityId     the activity id
     * @param request        the worklog
     * @return Worklog response
     */
    WorklogResponse createWorkLog(String organizationId, Long projectId, Long activityId, NewAdminWorklogRequest request);

    /**
     * prefill an worklog
     *
     * @param worklogBean    the object
     * @return true or false the response
     */
    Boolean createPrefillWorklog(WorklogBean worklogBean);
    /**
     * Update a worklog
     *
     * @param organizationId the organization id
     * @param projectId      the project id
     * @param activityId     the activity id
     * @param request        the update request
     * @return the updated worklog
     */
    WorklogResponse updateWorklog(String organizationId, Long projectId, Long activityId, UpdateWorklogRequest request);

    /**
     * Delete a worklog
     *
     * @param organizationId the organization id
     * @param projectId      the project id
     * @param activityId     the activity id
     * @param worklogId      the worklog id
     */
    void deleteWorklog(String organizationId, Long projectId, Long activityId, Long worklogId);

    /**
     * Update/confirm a list of the current user's existing worklogs. Returns a list of updated worklogs. If confirming the
     * worklog fails, the operation will continue until the list is exhausted.
     *
     * @param request list of update requests
     * @return list of updated worklogs
     */
    List<WorklogResponse> updateCurrentUserWorklogs(UpdateCurrentUserWorklogRequestList request);
}
