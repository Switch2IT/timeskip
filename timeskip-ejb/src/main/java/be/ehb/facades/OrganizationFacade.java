package be.ehb.facades;

import be.ehb.entities.organizations.MembershipBean;
import be.ehb.entities.organizations.OrganizationBean;
import be.ehb.entities.security.RoleBean;
import be.ehb.exceptions.OrganizationNotFoundException;
import be.ehb.factories.ExceptionFactory;
import be.ehb.factories.ResponseFactory;
import be.ehb.model.requests.*;
import be.ehb.model.responses.ActivityResponse;
import be.ehb.model.responses.OrganizationResponse;
import be.ehb.model.responses.ProjectResponse;
import be.ehb.security.ISecurityContext;
import be.ehb.storage.IStorageService;
import be.ehb.utils.ConventionUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@Default
public class OrganizationFacade implements IOrganizationFacade {

    private static final Logger log = LoggerFactory.getLogger(OrganizationFacade.class);

    @Inject
    private IStorageService storage;
    @Inject
    private ISecurityContext securityContext;

    @Override
    public List<OrganizationResponse> listOrganizations() {
        return storage.listOrganizations().stream()
                .map(ResponseFactory::createOrganizationResponse)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public OrganizationResponse get(String organizationId) {
        return ResponseFactory.createOrganizationResponse(storage.getOrganization(organizationId));
    }

    @Override
    public OrganizationResponse createOrganization(NewOrganizationRequest request) {
        OrganizationBean newOrg = new OrganizationBean();
        //Create the id string, remove funky characters, white space make it all lowercase
        String id = ConventionUtil.idFromName(request.getName());
        try {
            storage.getOrganization(id);
            throw ExceptionFactory.organizationAlreadyExistsException(id);
        } catch (OrganizationNotFoundException ex) {
            //Do nothing
        }

        newOrg.setId(id);
        newOrg.setName(request.getName());
        newOrg.setDescription(request.getDescription());
        newOrg = storage.createOrganization(newOrg);

        //Check if there is a role to be autogranted to the organization creator
        RoleBean role = storage.getAutoGrantRole();
        String userId = securityContext.getCurrentUser();
        if (role != null && StringUtils.isNotEmpty(userId)) {
            MembershipBean membership = new MembershipBean();
            membership.setRoleId(role.getId());
            membership.setUserId(userId);
            membership.setOrganizationId(id);
            storage.createMembership(membership);
            log.info("Created membership for user \"{}\" with role \"{}\"", userId, role.getName());
        }

        return ResponseFactory.createOrganizationResponse(newOrg);
    }

    @Override
    public OrganizationResponse updateOrganization(String organizationId, UpdateOrganizationRequest request) {
        OrganizationBean org = storage.getOrganization(organizationId);
        boolean changed = false;
        if (request.getDescription() != null) {
            org.setDescription(request.getDescription());
            changed = true;
        }
        if (StringUtils.isNotEmpty(request.getName())) {
            org.setName(request.getName());
            changed = true;
        }
        if (changed) {
            return ResponseFactory.createOrganizationResponse(storage.updateOrganization(org));
        }
        return null;
    }

    @Override
    public void deleteOrganization(String organizationId) {
        OrganizationBean org = storage.getOrganization(organizationId);
        storage.deleteOrganization(org);
    }

    @Override
    public List<ProjectResponse> listProjects(String organizationId) {
        return storage.listProjects(organizationId).stream()
                .map(ResponseFactory::createProjectResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ProjectResponse getProject(String organizationId, String projectId) {
        return ResponseFactory.createProjectResponse(storage.getProject(organizationId, projectId));
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