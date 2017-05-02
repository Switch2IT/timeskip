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
     * @return
     */
    List<ProjectDTO> listProjects();

    /**
     * Get project for given ID
     * @param projectId
     * @return
     */
    ProjectDTO getProject(String projectId);

    /**
     * Create a project
     * @param project
     * @return
     */
    ProjectDTO createProject(ProjectDTO project);

    /**
     * Update a project
     * @param project
     * @return
     */
    ProjectDTO updateProject(ProjectDTO project);

    /**
     * Delete a project
     * @param projectId
     */
    void deleteProject(String projectId);

    /**
     * List activities in project
     * @param projectId
     * @return
     */
    List<ActivityDTO> listProjectActivities(String projectId);

    /**
     * Get activity for given ID
     * @param projectId
     * @param activityId
     * @return
     */
    ActivityDTO getActivity(String projectId, String activityId);

    /**
     * Create an activity in a project
     * @param projectID
     * @param activity
     * @return
     */
    ActivityDTO createActivity(String projectID, ActivityDTO activity);

    /**
     * Update an activity
     * @param activity
     * @return
     */
    ActivityDTO updateActivity(ActivityDTO activity);

    /**
     * Delete an activity
     * @param activityId
     */
    void deleteActivity(String activityId);
}
