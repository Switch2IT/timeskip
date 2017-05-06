package be.ehb.facades;

import be.ehb.model.requests.*;
import be.ehb.model.responses.ActivityResponse;
import be.ehb.model.responses.OrganizationResponse;
import be.ehb.model.responses.ProjectResponse;

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
     * @param request the update request
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
    ProjectResponse getProject(String organizationId, String projectId);

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
    ProjectResponse updateProject(String organizationId, UpdateProjectRequest request);

    /**
     * Delete a project
     *
     * @param organizationId the organization id
     * @param projectId      the project id
     */
    void deleteProject(String organizationId, String projectId);

    /**
     * List activities in project
     *
     * @param organizationId the organization id
     * @param projectId      the project id
     * @return Activity
     */
    List<ActivityResponse> listProjectActivities(String organizationId, String projectId);

    /**
     * Get activity for given ID
     *
     * @param organizationId the organization id
     * @param projectId      the project id
     * @param activityId     the activity id
     * @return Activity
     */
    ActivityResponse getActivity(String organizationId, String projectId, String activityId);

    /**
     * Create an activity in a project
     *
     * @param organizationId the organization id
     * @param projectID      the project id
     * @param request        the activity
     * @return Activity
     */
    ActivityResponse createActivity(String organizationId, String projectID, NewActivityRequest request);

    /**
     * Update an activity
     *
     * @param organizationId the organization id
     * @param projectId      the project id
     * @param request        the update request
     * @return Activity
     */
    ActivityResponse updateActivity(String organizationId, String projectId, UpdateActivityRequest request);

    /**
     * Delete an activity
     *
     * @param organizationId the organization id\
     * @param projectId      the project id
     * @param activityId     the activity ID
     */
    void deleteActivity(String organizationId, String projectId, String activityId);

}
