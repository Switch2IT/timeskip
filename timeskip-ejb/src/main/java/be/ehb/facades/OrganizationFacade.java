package be.ehb.facades;

import be.ehb.model.requests.*;
import be.ehb.model.responses.ActivityResponse;
import be.ehb.model.responses.OrganizationResponse;
import be.ehb.model.responses.ProjectResponse;
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
public class OrganizationFacade implements IOrganizationFacade {

    private static final Logger log = LoggerFactory.getLogger(OrganizationFacade.class);

    @Override
    public List<OrganizationResponse> listOrganizations() {
        return null;
    }

    @Override
    public OrganizationResponse get(String organizationId) {
        return null;
    }

    @Override
    public OrganizationResponse createOrganization(NewOrganizationRequest request) {
        return null;
    }

    @Override
    public OrganizationResponse updateOrganization(UpdateOrganizationRequest request) {
        return null;
    }

    @Override
    public void deleteOrganization(String organizationId) {

    }

    @Override
    public List<ProjectResponse> listProjects(String organizationId) {
        return null;
    }

    @Override
    public ProjectResponse getProject(String organizationId, String projectId) {
        return null;
    }

    @Override
    public ProjectResponse createProject(String organizationId, NewProjectRequest request) {
        return null;
    }

    @Override
    public ProjectResponse updateProject(String organizationId, UpdateProjectRequest request) {
        return null;
    }

    @Override
    public void deleteProject(String organizationId, String projectId) {

    }

    @Override
    public List<ActivityResponse> listProjectActivities(String organizationId, String projectId) {
        return null;
    }

    @Override
    public ActivityResponse getActivity(String organizationId, String projectId, String activityId) {
        return null;
    }

    @Override
    public ActivityResponse createActivity(String organizationId, String projectID, NewActivityRequest request) {
        return null;
    }

    @Override
    public ActivityResponse updateActivity(String organizationId, String projectId, UpdateActivityRequest request) {
        return null;
    }

    @Override
    public void deleteActivity(String organizationId, String projectId, String activityId) {

    }
}