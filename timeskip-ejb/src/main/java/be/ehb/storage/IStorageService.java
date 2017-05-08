package be.ehb.storage;

import be.ehb.entities.config.ConfigBean;
import be.ehb.entities.mail.MailTemplateBean;
import be.ehb.entities.organizations.MembershipBean;
import be.ehb.entities.organizations.OrganizationBean;
import be.ehb.entities.projects.ActivityBean;
import be.ehb.entities.projects.ProjectBean;
import be.ehb.entities.projects.WorklogBean;
import be.ehb.entities.security.RoleBean;
import be.ehb.entities.users.PaygradeBean;
import be.ehb.entities.users.UserBean;
import be.ehb.mail.MailTopic;
import be.ehb.security.PermissionBean;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public interface IStorageService {

    //Get

    ActivityBean getActivity(Long activityId);

    ActivityBean getActivity(String organizationId, Long projectId, Long activityId);

    MailTemplateBean getMailTemplate(MailTopic topic);

    MembershipBean getMembership(Long membershipId);

    OrganizationBean getOrganization(String organizationId);

    PaygradeBean getPaygrade(Long paygradeId);

    ProjectBean getProject(String organizationId, Long projectId);

    RoleBean getRole(String roleId);

    UserBean getUser(String userId);

    WorklogBean getWorklog(String organizationId, Long projectId, Long activityId, Long worklogId);

    WorklogBean getWorklog(Long worklogId);

    //Create

    ActivityBean createActivity(ActivityBean activity);

    MembershipBean createMembership(MembershipBean membership);

    OrganizationBean createOrganization(OrganizationBean organization);

    PaygradeBean createPaygrade(PaygradeBean paygrade);

    ProjectBean createProject(ProjectBean project);

    UserBean createUser(UserBean user);

    WorklogBean createWorklog(WorklogBean worklog);

    //Create or Update

    MembershipBean createOrUpdateMembership(MembershipBean membership);

    //Update

    ActivityBean updateActivity(ActivityBean activity);

    ConfigBean updateConfig(ConfigBean config);

    MailTemplateBean updateMailTemplate(MailTemplateBean template);

    MembershipBean updateMembership(MembershipBean membership);

    OrganizationBean updateOrganization(OrganizationBean organization);

    PaygradeBean updatePaygrade(PaygradeBean paygrade);

    ProjectBean updateProject(ProjectBean project);

    UserBean updateUser(UserBean user);

    WorklogBean updateWorklog(WorklogBean worklog);

    //Delete

    void deleteActivity(ActivityBean activity);

    void deleteMembership(MembershipBean membership);

    void deleteOrganization(OrganizationBean organization);

    void deletePaygrade(PaygradeBean paygrade);

    void deleteProject(ProjectBean project);

    void deleteUser(UserBean user);

    void deleteWorklog(WorklogBean worklog);

    //List

    List<ActivityBean> listProjectActivities(String organizationId, Long projectId);

    List<MailTemplateBean> listMailTemplates();

    List<MembershipBean> listMemberships(String userId);

    List<OrganizationBean> listOrganizations();

    List<PaygradeBean> listPaygrades();

    List<ProjectBean> listProjects(String organizationId);

    List<UserBean> listUsers();

    List<WorklogBean> listActivityWorklogs(String organizationId, Long projectId, Long activityId);

    //Queries

    ConfigBean getDefaultConfig();

    RoleBean getAutoGrantRole();

    Set<MembershipBean> getMemberships(String userId);

    Set<PermissionBean> getPermissions(String userId);

    ActivityBean findActivityByName(String organizationId, Long projectId, String activityName);

    MembershipBean findMembershipByUserAndOrganization(String userId, String organizationId);

    OrganizationBean findOrganizationByName(String organizationName);

    PaygradeBean findPaygradeByName(String paygradeName);

    ProjectBean findProjectByName(String organizationId, String projectName);

    UserBean findUserByEmail(String email);

    List<UserBean> findUsersByFirstAndLastName(String firstName, String lastName);

    Long getUserLoggedMinutesForDay(String userId, Date day);
}
