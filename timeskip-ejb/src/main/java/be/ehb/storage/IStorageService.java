package be.ehb.storage;

import be.ehb.entities.config.ConfigBean;
import be.ehb.entities.organizations.MembershipBean;
import be.ehb.entities.organizations.OrganizationBean;
import be.ehb.entities.projects.ActivityBean;
import be.ehb.entities.projects.ProjectBean;
import be.ehb.entities.security.RoleBean;
import be.ehb.entities.users.UserBean;
import be.ehb.security.PermissionBean;

import java.util.List;
import java.util.Set;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public interface IStorageService {

    //Get

    ActivityBean getActivity(String organizationId, Long projectId, Long activityId);
    OrganizationBean getOrganization(String organizationId);
    ProjectBean getProject(String organizationId, Long projectId);
    RoleBean getRole(String roleId);
    UserBean getUser(String userId);

    //Create

    ActivityBean createActivity(ActivityBean activity);
    MembershipBean createMembership(MembershipBean membership);
    OrganizationBean createOrganization(OrganizationBean organization);
    ProjectBean createProject(ProjectBean project);
    UserBean createUser(UserBean user);

    //Update

    ActivityBean updateActivity(ActivityBean activity);
    OrganizationBean updateOrganization(OrganizationBean organization);
    ProjectBean updateProject(ProjectBean project);

    //Delete

    void deleteActivity(ActivityBean activity);
    void deleteOrganization(OrganizationBean organization);
    void deleteProject(ProjectBean project);

    //List

    List<ActivityBean> listProjectActivities(String organizationId, Long projectId);
    List<OrganizationBean> listOrganizations();
    List<ProjectBean> listProjects(String organizationId);
    List<UserBean> listUsers();

    //Queries

    ConfigBean getDefaultConfig();
    RoleBean getAutoGrantRole();
    Set<MembershipBean> getMemberships(String userId);
    Set<PermissionBean> getPermissions(String userId);

    ActivityBean findActivityByName(String organizationId, Long projectId, String activityName);
    OrganizationBean findOrganizationByName(String organizationName);
    ProjectBean findProjectByName(String organizationId, String projectName);
}
