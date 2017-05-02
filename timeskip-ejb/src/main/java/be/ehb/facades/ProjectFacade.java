package be.ehb.facades;

import be.ehb.model.activities.ActivityDTO;
import be.ehb.model.projects.ProjectDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.enterprise.inject.Default;
import java.util.List;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@Default
public class ProjectFacade implements IProjectFacade {

    private static final Logger log = LoggerFactory.getLogger(ProjectFacade.class);

    @Override
    public List<ProjectDTO> listProjects() {
        return null;
    }

    @Override
    public ProjectDTO getProject(String projectId) {
        return null;
    }

    @Override
    public ProjectDTO createProject(ProjectDTO project) {
        return null;
    }

    @Override
    public ProjectDTO updateProject(ProjectDTO project) {
        return null;
    }

    @Override
    public void deleteProject(String projectId) {

    }

    @Override
    public List<ActivityDTO> listProjectActivities(String projectId) {
        return null;
    }

    @Override
    public ActivityDTO getActivity(String projectId, String activityId) {
        return null;
    }

    @Override
    public ActivityDTO createActivity(String projectID, ActivityDTO activity) {
        return null;
    }

    @Override
    public ActivityDTO updateActivity(ActivityDTO activity) {
        return null;
    }

    @Override
    public void deleteActivity(String activityId) {

    }
}