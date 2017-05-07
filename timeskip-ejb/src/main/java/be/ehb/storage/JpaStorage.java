package be.ehb.storage;

import be.ehb.entities.config.ConfigBean;
import be.ehb.entities.organizations.MembershipBean;
import be.ehb.entities.organizations.OrganizationBean;
import be.ehb.entities.projects.ActivityBean;
import be.ehb.entities.projects.ProjectBean;
import be.ehb.entities.projects.WorklogBean;
import be.ehb.entities.security.RoleBean;
import be.ehb.entities.users.UserBean;
import be.ehb.entities.users.UsersWorkLoadActivityBO;
import be.ehb.factories.ExceptionFactory;
import be.ehb.security.PermissionBean;
import be.ehb.security.PermissionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;
import javax.persistence.NoResultException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Guillaume Vandecasteele / Patrick Van den Bussche
 * @since 2017
 */
@ApplicationScoped
@Default
public class JpaStorage extends AbstractJpaStorage implements IStorageService {

    private static final Logger log = LoggerFactory.getLogger(JpaStorage.class);

    @Override
    public ActivityBean getActivity(String organizationId, Long projectId, Long activityId) {
        try {
            return getActiveEntityManager()
                    .createQuery("SELECT a FROM ActivityBean a JOIN a.project p JOIN p.organization o WHERE a.id = :aId AND p.id = :pId AND o.id = :orgId", ActivityBean.class)
                    .setParameter("aId", activityId)
                    .setParameter("pId", projectId)
                    .setParameter("orgId", organizationId)
                    .getSingleResult();
        } catch (NoResultException ex) {
            throw ExceptionFactory.activityNotFoundException(activityId);
        }
    }

    @Override
    public OrganizationBean getOrganization(String organizationId) {
        OrganizationBean org = super.get(organizationId, OrganizationBean.class);
        if (org == null) throw ExceptionFactory.organizationNotFoundException(organizationId);
        return org;
    }

    @Override
    public ProjectBean getProject(String organizationId, Long projectId) {
        try {
            return getActiveEntityManager()
                    .createQuery("SELECT p FROM ProjectBean p JOIN p.organization o WHERE p.id = :pId AND o.id = :orgId", ProjectBean.class)
                    .setParameter("pId", projectId)
                    .setParameter("orgId", organizationId)
                    .getSingleResult();
        } catch (NoResultException ex) {
            throw ExceptionFactory.projectNotFoundException(projectId);
        }
    }

    @Override
    public RoleBean getRole(String roleId) {
        RoleBean role = super.get(roleId, RoleBean.class);
        if (role == null) throw ExceptionFactory.roleNotFoundException(roleId);
        return role;
    }

    @Override
    public UserBean getUser(String userId) {
        UserBean user = super.get(userId, UserBean.class);
        if (user == null) throw ExceptionFactory.userNotFoundException(userId);
        return user;
    }

    @Override
    public WorklogBean getWorklog(String organizationId, Long projectId, Long activityId, Long worklogId) {
        try {
            return getActiveEntityManager()
                    .createQuery("SELECT w FROM WorklogBean w JOIN w.activity a JOIN a.project p JOIN p.organization o WHERE w.id = :wId AND a.id = :aId AND p.id = :pId AND o.id = :orgId", WorklogBean.class)
                    .setParameter("wId", worklogId)
                    .setParameter("aId", activityId)
                    .setParameter("pId", projectId)
                    .setParameter("orgId", organizationId)
                    .getSingleResult();
        } catch (NoResultException ex) {
            throw ExceptionFactory.activityNotFoundException(activityId);
        }
    }

    @Override
    public WorklogBean getWorklog(Long worklogId) {
        WorklogBean worklog = super.get(worklogId, WorklogBean.class);
        if (worklog == null) throw ExceptionFactory.worklogNotFoundException(worklogId);
        return worklog;
    }

    @Override
    public ActivityBean createActivity(ActivityBean activity) {
        return super.create(activity);
    }

    @Override
    public ConfigBean updateConfig(ConfigBean config) {
        return super.update(config);
    }

    @Override
    public MembershipBean createMembership(MembershipBean membership) {
        return super.create(membership);
    }

    @Override
    public OrganizationBean createOrganization(OrganizationBean organization) {
        return super.create(organization);
    }

    @Override
    public ProjectBean createProject(ProjectBean project) {
        return super.create(project);
    }

    @Override
    public UserBean createUser(UserBean user) {
        return super.create(user);
    }

    @Override
    public WorklogBean createWorklog(WorklogBean worklog) {
        return super.create(worklog);
    }

    @Override
    public ActivityBean updateActivity(ActivityBean activity) {
        return super.update(activity);
    }

    @Override
    public OrganizationBean updateOrganization(OrganizationBean organization) {
        return super.update(organization);
    }

    @Override
    public ProjectBean updateProject(ProjectBean project) {
        return super.update(project);
    }

    @Override
    public WorklogBean updateWorklog(WorklogBean worklog) {
        return super.update(worklog);
    }

    @Override
    public void deleteActivity(ActivityBean activity) {
        super.delete(activity);
    }

    @Override
    public void deleteOrganization(OrganizationBean organization) {
        super.delete(organization);
    }

    @Override
    public void deleteProject(ProjectBean project) {
        super.delete(project);
    }

    @Override
    public void deleteWorklog(WorklogBean worklog) {
        super.delete(worklog);
    }

    @Override
    public List<ActivityBean> listProjectActivities(String organizationId, Long projectId) {
        ProjectBean proj = getProject(organizationId, projectId);
        return getActiveEntityManager()
                .createQuery("SELECT a FROM ActivityBean a JOIN a.project p JOIN p.organization o WHERE o.id = :orgId AND p.id = :pId", ActivityBean.class)
                .setParameter("orgId", proj.getOrganization().getId())
                .setParameter("pId", proj.getId())
                .getResultList();
    }

    @Override
    public List<OrganizationBean> listOrganizations() {
        return getActiveEntityManager()
                .createQuery("SELECT o FROM OrganizationBean o", OrganizationBean.class)
                .getResultList();
    }

    @Override
    public List<UserBean> listUsers() {
        return getActiveEntityManager().createQuery("SELECT u FROM UserBean u", UserBean.class).getResultList();
    }

    @Override
    public List<ProjectBean> listProjects(String organizationId) {
        OrganizationBean org = getOrganization(organizationId);
        return getActiveEntityManager()
                .createQuery("SELECT p FROM ProjectBean p JOIN p.organization o WHERE o.id = :orgId", ProjectBean.class)
                .setParameter("orgId", org.getId())
                .getResultList();
    }

    @Override
    public List<WorklogBean> listActivityWorklogs(String organizationId, Long projectId, Long activityId) {
        ActivityBean activity = getActivity(organizationId, projectId, activityId);
        return getActiveEntityManager()
                .createQuery("SELECT w FROM WorklogBean w WHERE w.activity = :activity", WorklogBean.class)
                .setParameter("activity", activity)
                .getResultList();
    }

    @Override
    public ConfigBean getDefaultConfig() {
        try {
            return getActiveEntityManager().createQuery("SELECT c FROM ConfigBean c WHERE c.defaultConfig = TRUE", ConfigBean.class).getSingleResult();
        } catch (NoResultException ex) {
            log.error("No results found: {}", ex.getMessage());
            throw ExceptionFactory.storageException("No configuration found.");
        }
    }

    @Override
    public RoleBean getAutoGrantRole() {
        try {
            return getActiveEntityManager()
                    .createQuery("SELECT r FROM RoleBean r WHERE r.autoGrant = TRUE", RoleBean.class)
                    .getSingleResult();
        } catch (NoResultException ex) {
            log.warn("No role configured for auto-grant");
            return null;
        }
    }

    @Override
    public Set<MembershipBean> getMemberships(String userId) {
        Set<MembershipBean> rval = new HashSet<>();
        rval.addAll(getActiveEntityManager()
                .createQuery("SELECT m FROM MembershipBean m WHERE m.userId = :userId", MembershipBean.class)
                .setParameter("userId", userId)
                .getResultList());
        return rval;
    }

    @Override
    public Set<PermissionBean> getPermissions(String userId) {
        Set<PermissionBean> rval = new HashSet<>();
        Set<MembershipBean> memberships = getMemberships(userId);
        for (MembershipBean membership : memberships) {
            RoleBean role = getRole(membership.getRoleId());
            for (PermissionType permission : role.getPermissions()) {
                PermissionBean pb = new PermissionBean();
                pb.setName(permission);
                pb.setOrganizationId(membership.getOrganizationId());
                rval.add(pb);
            }
        }
        return rval;
    }

    @Override
    public ActivityBean findActivityByName(String organizationId, Long projectId, String activityName) {
        try {
            return getActiveEntityManager()
                    .createQuery("SELECT a FROM ActivityBean a JOIN a.project p JOIN p.organization o WHERE o.id = :orgId AND p.id = :pId AND a.name = :aName", ActivityBean.class)
                    .setParameter("orgId", organizationId)
                    .setParameter("pId", projectId)
                    .setParameter("aName", activityName)
                    .getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    public OrganizationBean findOrganizationByName(String organizationName) {
        try {
            return getActiveEntityManager()
                    .createQuery("SELECT o FROM OrganizationBean o WHERE o.name = :orgName", OrganizationBean.class)
                    .setParameter("orgName", organizationName)
                    .getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    public ProjectBean findProjectByName(String organizationId, String projectName) {
        try {
            return getActiveEntityManager()
                    .createQuery("SELECT p FROM ProjectBean p JOIN p.organization o WHERE o.id = :orgId AND p.name = :pName", ProjectBean.class)
                    .setParameter("orgId", organizationId)
                    .setParameter("pName", projectName)
                    .getSingleResult();
        } catch (NoResultException ex) {
            log.info("No project with name \"{}\" found in organization \"{}\"", organizationId, projectName);
            return null;
        }
    }

    @Override
    public Long getUserLoggedMinutesForDay(String userId, Date day) {
        return getActiveEntityManager()
                .createQuery("SELECT SUM(w.loggedMinutes) FROM WorklogBean w WHERE w.userId = :user AND w.day = :day", Long.class)
                .setParameter("user", userId)
                .setParameter("day", day)
                .getSingleResult();
    }

    @Override
    public List<UsersWorkLoadActivityBO> listUsersWorkloadActivity(Date day) {
        return getActiveEntityManager()
                .createQuery("SELECT NEW be.ehb.entities.users.UsersWorkLoadActivityBO(u.id, u.firstName, u.lastName, u.email, w.day, w.loggedMinutes, w.confirmed, a.description) " +
                        "FROM UserBean u, WorklogBean w " +
                        "JOIN w.activity a " +
                        "WHERE u.id = w.userId AND w.day < :date AND w.confirmed = FALSE " +
                        "ORDER BY u.id, w.day", UsersWorkLoadActivityBO.class)
                .setParameter("date", day)
                .getResultList();
    }
}