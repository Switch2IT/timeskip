package be.ehb.facades;

import be.ehb.model.activities.ActivityDTO;
import be.ehb.model.projects.ProjectDTO;

import java.util.List;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public interface IProjectFacade {
    /**
     * List projects
     *
     * @return List of projects
     */
    List<ProjectDTO> listProjects();

    /**
     * Get project for given ID
     *
     * @param projectId the project id
     * @return Project
     */
    ProjectDTO getProject(String projectId);

    /**
     * Create a project
     *
     * @param project the project
     * @return Project
     */
    ProjectDTO createProject(ProjectDTO project);

    /**
     * Update a project
     *
     * @param project the project
     * @return Project
     */
    ProjectDTO updateProject(ProjectDTO project);

    /**
     * Delete a project
     *
     * @param projectId the project id
     */
    void deleteProject(String projectId);

    /**
     * List activities in project
     *
     * @param projectId the project id
     * @return Activity
     */
    List<ActivityDTO> listProjectActivities(String projectId);

    /**
     * Get activity for given ID
     *
     * @param projectId the project id
     * @param activityId the activity id
     * @return Activity
     */
    ActivityDTO getActivity(String projectId, String activityId);

    /**
     * Create an activity in a project
     *
     * @param projectID the project id
     * @param activity the activity
     * @return Activity
     */
    ActivityDTO createActivity(String projectID, ActivityDTO activity);

    /**
     * Update an activity
     *
     * @param activity the activity
     * @return Activity
     */
    ActivityDTO updateActivity(ActivityDTO activity);

    /**
     * Delete an activity
     *
     * @param activityId the activity ID
     */
    void deleteActivity(String activityId);
}
