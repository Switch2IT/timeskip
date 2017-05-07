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
        ProjectBean p = getProject(organizationId, projectId);
        ActivityBean a = super.get(activityId, ActivityBean.class);
        if (a == null || !a.getProject().equals(p)) throw ExceptionFactory.activityNotFoundException(activityId);
        return a;
    }

    @Override
    public OrganizationBean getOrganization(String organizationId) {
        OrganizationBean org = super.get(organizationId, OrganizationBean.class);
        if (org == null) throw ExceptionFactory.organizationNotFoundException(organizationId);
        return org;
    }

    @Override
    public ProjectBean getProject(String organizationId, Long projectId) {
        OrganizationBean o = getOrganization(organizationId);
        ProjectBean p = super.get(projectId, ProjectBean.class);
        if (p == null || !p.getOrganization().equals(o)) throw ExceptionFactory.projectNotFoundException(projectId);
        return p;
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
        ActivityBean activity = getActivity(organizationId, projectId, activityId);
        WorklogBean worklog = super.get(worklogId, WorklogBean.class);
        if (worklog == null || !worklog.getActivity().equals(activity))
            throw ExceptionFactory.worklogNotFoundException(worklogId);
        return worklog;
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
                .createQuery("SELECT a FROM ActivityBean a JOIN a.project p JOIN p.organization o WHERE o.id = :orgId AND p.id = :pId")
                .setParameter("orgId", proj.getOrganization().getId())
                .setParameter("pId", proj.getId())
                .getResultList();
    }

    @Override
    public List<OrganizationBean> listOrganizations() {
        return getActiveEntityManager()
                .createQuery("SELECT o FROM OrganizationBean o")
                .getResultList();
    }

    @Override
    public List<UserBean> listUsers() {
        return getActiveEntityManager().createQuery("SELECT u FROM UserBean u").getResultList();
    }

    @Override
    public List<ProjectBean> listProjects(String organizationId) {
        OrganizationBean org = getOrganization(organizationId);
        return getActiveEntityManager()
                .createQuery("SELECT p FROM ProjectBean p JOIN p.organization o WHERE o.id = :orgId")
                .setParameter("orgId", org.getId())
                .getResultList();
    }

    @Override
    public List<WorklogBean> listActivityWorklogs(String organizationId, Long projectId, Long activityId) {
        ActivityBean activity = getActivity(organizationId, projectId, activityId);
        return getActiveEntityManager()
                .createQuery("SELECT w FROM WorklogBean w WHERE w.activity = :activity")
                .setParameter("activity", activity)
                .getResultList();
    }

    @Override
    public ConfigBean getDefaultConfig() {
        try {
            return (ConfigBean) getActiveEntityManager().createQuery("SELECT c FROM ConfigBean c WHERE c.defaultConfig = TRUE").getSingleResult();
        } catch (NoResultException ex) {
            log.error("No results found: {}", ex.getMessage());
            return null;
        }
    }

    @Override
    public RoleBean getAutoGrantRole() {
        try {
            return (RoleBean) getActiveEntityManager()
                    .createQuery("SELECT r FROM RoleBean r WHERE r.autoGrant = TRUE")
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
                .createQuery("SELECT m FROM MembershipBean m WHERE m.userId = :userId")
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
            return (ActivityBean) getActiveEntityManager()
                    .createQuery("SELECT a FROM ActivityBean a JOIN a.project p JOIN p.organization o WHERE o.id = :orgId AND p.id = :pId AND a.name = :aName")
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
            return (OrganizationBean) getActiveEntityManager()
                    .createQuery("SELECT o FROM OrganizationBean o WHERE o.name = :orgName")
                    .setParameter("orgName", organizationName)
                    .getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    public ProjectBean findProjectByName(String organizationId, String projectName) {
        try {
            return (ProjectBean) getActiveEntityManager()
                    .createQuery("SELECT p FROM ProjectBean p JOIN p.organization o WHERE o.id = :orgId AND p.name = :pName")
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
        return (Long) getActiveEntityManager()
                .createQuery("SELECT SUM(w.loggedMinutes) FROM WorklogBean w WHERE w.userId = :user AND w.day = :day")
                .setParameter("user", userId)
                .setParameter("day", day)
                .getSingleResult();
    }

    @Override
    public List<UsersWorkLoadActivityBO> listUsersWorkloadActivity(Date day) {
        StringBuilder sbqr = new StringBuilder();
        sbqr.append("select u.id, u.first_name, u.last_name, u.email, wl.day, ac.description, wl.logged_minutes, wl.confirmed");
        sbqr.append("from users u");
        sbqr.append("inner join worklogs wl");
        sbqr.append("on u.id = wl.user_id");
        sbqr.append("inner join activities ac");
        sbqr.append("on wl.activity_id = ac.id");
        sbqr.append("where wl.day < :date");
        return getActiveEntityManager()
                .createQuery(sbqr.toString())
                .setParameter("date", day)
                .getResultList();
    }
}