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
import be.ehb.factories.ExceptionFactory;
import be.ehb.mail.MailTopic;
import be.ehb.model.backup.*;
import be.ehb.model.requests.RestoreBackupRequest;
import be.ehb.security.PermissionBean;
import be.ehb.security.PermissionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;
import javax.persistence.NoResultException;
import java.util.*;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
@ApplicationScoped
@Default
public class JpaStorage extends AbstractJpaStorage implements IStorageService {

    private static final Logger log = LoggerFactory.getLogger(JpaStorage.class);

    @Override
    public ActivityBean getActivity(Long activityId) {
        ActivityBean activity = super.get(activityId, ActivityBean.class);
        if (activity == null) throw ExceptionFactory.activityNotFoundException(activityId);
        return activity;
    }

    @Override
    public ActivityBean getActivity(String organizationId, Long projectId, Long activityId) {
        try {
            return (ActivityBean) getActiveEntityManager()
                    .createQuery("SELECT a FROM ActivityBean a JOIN a.project p JOIN p.organization o WHERE a.id = :aId AND p.id = :pId AND o.id = :orgId")
                    .setParameter("aId", activityId)
                    .setParameter("pId", projectId)
                    .setParameter("orgId", organizationId)
                    .getSingleResult();
        } catch (NoResultException ex) {
            throw ExceptionFactory.activityNotFoundException(activityId);
        }
    }

    @Override
    public MailTemplateBean getMailTemplate(MailTopic topic) {
        MailTemplateBean template = super.get(topic, MailTemplateBean.class);
        if (template == null) throw ExceptionFactory.mailTemplateNotFoundException(topic);
        return template;
    }

    @Override
    public MembershipBean getMembership(Long membershipId) {
        MembershipBean membership = super.get(membershipId, MembershipBean.class);
        if (membership == null) throw ExceptionFactory.membershipNotFoundException(membershipId);
        return membership;
    }

    @Override
    public OrganizationBean getOrganization(String organizationId) {
        OrganizationBean org = super.get(organizationId, OrganizationBean.class);
        if (org == null) throw ExceptionFactory.organizationNotFoundException(organizationId);
        return org;
    }

    @Override
    public PaygradeBean getPaygrade(Long paygradeId) {
        PaygradeBean paygrade = super.get(paygradeId, PaygradeBean.class);
        if (paygrade == null) throw ExceptionFactory.paygradeNotFoundException(paygradeId);
        return paygrade;
    }

    @Override
    public ProjectBean getProject(String organizationId, Long projectId) {
        try {
            return (ProjectBean) getActiveEntityManager()
                    .createQuery("SELECT p FROM ProjectBean p JOIN p.organization o WHERE p.id = :pId AND o.id = :orgId")
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
            return (WorklogBean) getActiveEntityManager()
                    .createQuery("SELECT w FROM WorklogBean w JOIN w.activity a JOIN a.project p JOIN p.organization o WHERE w.id = :wId AND a.id = :aId AND p.id = :pId AND o.id = :orgId")
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
    public MembershipBean createMembership(MembershipBean membership) {
        return super.create(membership);
    }

    @Override
    public OrganizationBean createOrganization(OrganizationBean organization) {
        return super.create(organization);
    }

    @Override
    public PaygradeBean createPaygrade(PaygradeBean paygrade) {
        return super.create(paygrade);
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
    public MembershipBean createOrUpdateMembership(MembershipBean membership) {
        MembershipBean rval = null;
        MembershipBean existing = findMembershipByUserAndOrganization(membership.getUserId(), membership.getOrganizationId());
        if (existing != null) {
            existing.setRoleId(membership.getRoleId());
            return updateMembership(membership);
        } else {
            return createMembership(membership);
        }
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    private void deleteAll() {

    }

    @Override
    public void restore(RestoreBackupRequest backup) {
        getActiveEntityManager().createQuery("DELETE FROM WorklogBean w").executeUpdate();
        getActiveEntityManager().createQuery("DELETE FROM PaygradeBean p").executeUpdate();
        getActiveEntityManager().createQuery("DELETE FROM MembershipBean m").executeUpdate();
        getActiveEntityManager().createQuery("DELETE FROM MailTemplateBean m").executeUpdate();
        getActiveEntityManager().createQuery("DELETE FROM ConfigBean c").executeUpdate();
        getActiveEntityManager().createQuery("DELETE FROM ActivityBean a").executeUpdate();
        getActiveEntityManager().createQuery("DELETE FROM ProjectBean p").executeUpdate();
        getActiveEntityManager().createQuery("DELETE FROM OrganizationBean o").executeUpdate();
        getActiveEntityManager().createQuery("DELETE FROM RoleBean r").executeUpdate();
        getActiveEntityManager().createQuery("DELETE FROM UserBean u").executeUpdate();

        restoreConfigs(backup.getConfigurations());
        restoreMailTemplates(backup.getMailTemplates());
        restoreRoles(backup.getRoles());

        Map<String, OrganizationBean> sortedOrgs = backup.getOrganizations() == null ? new HashMap<>() : restoreOrganizations(backup.getOrganizations());
        Map<Long, ProjectBean> sortedProjects = backup.getProjects() == null ? new HashMap<>() : restoreProjects(backup.getProjects(), sortedOrgs);
        Map<Long, PaygradeBean> sortedPaygrades = backup.getPaygrades() == null ? new HashMap<>() : restorePaygrades(backup.getPaygrades());
        Map<String, UserBean> sortedUsers = backup.getUsers() == null ? new HashMap<>() : restoreUsers(backup.getUsers(), sortedPaygrades);
        if (backup.getMemberships() != null) restoreMemberships(backup.getMemberships());
        Map<Long, ActivityBean> sortedActivities = backup.getActivities() == null ? new HashMap<>() : restoreActivities(backup.getActivities(), sortedProjects);
        if (backup.getAssignments() != null)
            restoreProjectAssignments(backup.getAssignments(), sortedProjects, sortedUsers);
        if (backup.getWorklogs() != null) restoreWorklogs(backup.getWorklogs(), sortedActivities);
    }



    @Override
    public ActivityBean updateActivity(ActivityBean activity) {
        return super.update(activity);
    }

    @Override
    public ConfigBean updateConfig(ConfigBean config) {
        return super.update(config);
    }

    @Override
    public OrganizationBean updateOrganization(OrganizationBean organization) {
        return super.update(organization);
    }

    @Override
    public PaygradeBean updatePaygrade(PaygradeBean paygrade) {
        return super.update(paygrade);
    }

    @Override
    public MailTemplateBean updateMailTemplate(MailTemplateBean template) {
        return super.update(template);
    }

    @Override
    public MembershipBean updateMembership(MembershipBean membership) {
        return super.update(membership);
    }

    @Override
    public ProjectBean updateProject(ProjectBean project) {
        return super.update(project);
    }

    @Override
    public UserBean updateUser(UserBean user) {
        return super.update(user);
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
    public void deleteMembership(MembershipBean membership) {
        super.delete(membership);
    }

    @Override
    public void deletePaygrade(PaygradeBean paygrade) {
        super.delete(paygrade);
    }

    @Override
    public void deleteProject(ProjectBean project) {
        super.delete(project);
    }

    @Override
    public void deleteUser(UserBean user) {
        super.delete(user);
    }

    @Override
    public void deleteWorklog(WorklogBean worklog) {
        super.delete(worklog);
    }

    @Override
    public List<ActivityBean> listActivities() {
        return getActiveEntityManager().createQuery("SELECT a FROM ActivityBean a", ActivityBean.class).getResultList();
    }

    @Override
    public List<ConfigBean> listConfigs() {
        return getActiveEntityManager().createQuery("SELECT c FROM ConfigBean c", ConfigBean.class).getResultList();
    }

    @Override
    public List<MailTemplateBean> listMailTemplates() {
        return getActiveEntityManager().createQuery("SELECT m FROM MailTemplateBean m", MailTemplateBean.class).getResultList();
    }

    @Override
    public List<MembershipBean> listUserMemberships(String userId) {
        return getActiveEntityManager()
                .createQuery("SELECT m FROM MembershipBean m WHERE m.userId = :uId", MembershipBean.class)
                .setParameter("uId", userId)
                .getResultList();
    }

    @Override
    public List<MembershipBean> listMemberships() {
        return getActiveEntityManager().createQuery("SELECT m FROM MembershipBean m", MembershipBean.class).getResultList();
    }

    @Override
    public List<OrganizationBean> listOrganizations() {
        return getActiveEntityManager()
                .createQuery("SELECT o FROM OrganizationBean o")
                .getResultList();
    }

    @Override
    public List<PaygradeBean> listPaygrades() {
        return getActiveEntityManager().createQuery("SELECT p FROM PaygradeBean p", PaygradeBean.class).getResultList();
    }

    @Override
    public List<ProjectBean> listOrganizationProjects(String organizationId) {
        OrganizationBean org = getOrganization(organizationId);
        return getActiveEntityManager()
                .createQuery("SELECT p FROM ProjectBean p JOIN p.organization o WHERE o.id = :orgId")
                .setParameter("orgId", org.getId())
                .getResultList();
    }

    @Override
    public List<ProjectBean> listProjects() {
        return getActiveEntityManager().createQuery("SELECT p FROM ProjectBean p", ProjectBean.class).getResultList();
    }

    @Override
    public List<RoleBean> listRoles() {
        return getActiveEntityManager().createQuery("SELECT r FROM RoleBean r", RoleBean.class).getResultList();
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
    public List<UserBean> listUsers() {
        return getActiveEntityManager().createQuery("SELECT u FROM UserBean u").getResultList();
    }

    @Override
    public List<WorklogBean> listWorklogs() {
        return getActiveEntityManager().createQuery("SELECT w FROM WorklogBean w", WorklogBean.class).getResultList();
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
    public List<WorklogBean> listUserWorklogs(String userId) {
        return getActiveEntityManager()
                .createQuery("SELECT w FROM WorklogBean w WHERE w.userId = :uId", WorklogBean.class)
                .setParameter("uId", userId)
                .getResultList();
    }

    @Override
    public ConfigBean getDefaultConfig() {
        try {
            return (ConfigBean) getActiveEntityManager().createQuery("SELECT c FROM ConfigBean c WHERE c.defaultConfig = TRUE").getSingleResult();
        } catch (NoResultException ex) {
            log.error("No results found: {}", ex.getMessage());
            throw ExceptionFactory.defaultConfigNotFoundException();
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
    public MembershipBean findMembershipByUserAndOrganization(String userId, String organizationId) {
        try {
            return getActiveEntityManager()
                    .createQuery("SELECT m FROM MembershipBean m WHERE m.userId = :uId AND m.organizationId = :oId", MembershipBean.class)
                    .setParameter("uId", userId)
                    .setParameter("oId", organizationId)
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
    public PaygradeBean findPaygradeByName(String paygradeName) {
        try {
            return getActiveEntityManager()
                    .createQuery("SELECT p FROM PaygradeBean p WHERE p.name = :pName", PaygradeBean.class)
                    .setParameter("pName", paygradeName)
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
    public UserBean findUserByEmail(String email) {
        try {
            return getActiveEntityManager()
                    .createQuery("SELECT u FROM UserBean u WHERE u.email = :mail", UserBean.class)
                    .setParameter("mail", email)
                    .getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    public List<UserBean> findUsersByFirstAndLastName(String firstName, String lastName) {
        return getActiveEntityManager()
                .createQuery("SELECT u FROM UserBean u WHERE u.firstName = :fName AND u.lastName = :lName", UserBean.class)
                .setParameter("fName", firstName)
                .setParameter("lName", lastName)
                .getResultList();
    }

    @Override
    public Long getUserLoggedMinutesForDay(String userId, Date day) {
        return (Long) getActiveEntityManager()
                .createQuery("SELECT SUM(w.loggedMinutes) FROM WorklogBean w WHERE w.userId = :user AND w.day = :day")
                .setParameter("user", userId)
                .setParameter("day", day)
                .getSingleResult();
    }

    //Private helper methods

    private void restoreRoles(Set<RoleBackup> rs) {
        for (RoleBackup r : rs) {
            RoleBean nr = new RoleBean();
            nr.setId(r.getId());
            nr.setName(r.getName());
            nr.setDescription(r.getDescription());
            nr.setAutoGrant(r.getAutoGrant());
            nr.setPermissions(new ArrayList<>(r.getPermissions()));
            super.update(nr);
        }
    }

    private void restoreConfigs(Set<ConfigurationBackup> cbs) {
        for (ConfigurationBackup c : cbs) {
            ConfigBean nc = new ConfigBean();
            nc.setId(c.getId());
            nc.setConfigPath(c.getConfigPath());
            nc.setDefaultConfig(c.getDefaultConfig());
            nc.setDayOfMonthlyReminderEmail(c.getDayOfMonthlyReminderEmail());
            nc.setLastDayOfMonth(c.getLastDayOfMonth());
            super.update(nc);
        }
    }

    private void restoreMailTemplates(Set<MailTemplateBackup> mbs) {
        for (MailTemplateBackup m : mbs) {
            MailTemplateBean nm = new MailTemplateBean();
            nm.setId(m.getId());
            nm.setSubject(m.getSubject());
            nm.setContent(m.getContent());
            super.update(nm);
        }
    }

    private Map<Long, PaygradeBean> restorePaygrades(Set<PaygradeBackup> pbs) {
        Map<Long, PaygradeBean> sortedPaygrades = new HashMap<>();
        for (PaygradeBackup p : pbs) {
            PaygradeBean np = new PaygradeBean();
            np.setId(p.getId());
            np.setName(p.getName());
            np.setDescription(p.getDescription());
            np.setHourlyRate(p.getHourlyRate());
            sortedPaygrades.put(p.getId(), super.update(np));
        }
        return sortedPaygrades;
    }

    private Map<String, OrganizationBean> restoreOrganizations(Set<OrganizationBackup> obs) {
        Map<String, OrganizationBean> sortedOrgs = new HashMap<>();
        for (OrganizationBackup o : obs) {
            OrganizationBean no = new OrganizationBean();
            no.setId(o.getId());
            no.setName(o.getName());
            no.setDescription(o.getDescription());
            sortedOrgs.put(o.getId(), super.update(no));
        }
        return sortedOrgs;
    }

    private Map<Long, ProjectBean> restoreProjects(Set<ProjectBackup> pbs, Map<String, OrganizationBean> os) {
        Map<Long, ProjectBean> sortedProjects = new HashMap<>();
        for (ProjectBackup p : pbs) {
            ProjectBean np = new ProjectBean();
            np.setId(p.getId());
            np.setName(p.getName());
            np.setDescription(p.getDescription());
            np.setBillOvertime(p.getBillOvertime());
            np.setAllowOvertime(np.getAllowOvertime());
            np.setOrganization(os.get(p.getOrganizationId()));
            sortedProjects.put(p.getId(), super.update(np));
        }
        return sortedProjects;
    }

    private Map<Long, ActivityBean> restoreActivities(Set<ActivityBackup> abs, Map<Long, ProjectBean> ps) {
        Map<Long, ActivityBean> sortedActivities = new HashMap<>();
        for (ActivityBackup a : abs) {
            ActivityBean na = new ActivityBean();
            na.setId(a.getId());
            na.setName(a.getName());
            na.setDescription(a.getDescription());
            na.setBillable(a.getBillable());
            na.setProject(ps.get(a.getProjectId()));
            sortedActivities.put(a.getId(), super.update(na));
        }
        return sortedActivities;
    }

    private Map<String, UserBean> restoreUsers(Set<UserBackup> ubs, Map<Long, PaygradeBean> pgs) {
        Map<String, UserBean> sortedUsers = new HashMap<>();
        for (UserBackup u : ubs) {
            UserBean nu = new UserBean();
            nu.setId(u.getId());
            nu.setFirstName(u.getFirstName());
            nu.setLastName(u.getLastName());
            nu.setEmail(u.getEmail());
            nu.setAdmin(u.getAdmin());
            nu.setPaygrade(pgs.get(u.getPaygradeId()));
            nu.setDefaultHoursPerDay(u.getDefaultHoursPerDay());
            nu.setDefaultActivity(u.getDefaultActivityId());
            nu.setWorkdays(u.getWorkDays());
            sortedUsers.put(u.getId(), super.update(nu));
        }
        return sortedUsers;
    }

    private void restoreMemberships(Set<MembershipBackup> mbs) {
        for (MembershipBackup m : mbs) {
            MembershipBean nm = new MembershipBean();
            nm.setId(m.getId());
            nm.setUserId(m.getUserId());
            nm.setOrganizationId(m.getOrganizationId());
            nm.setRoleId(m.getRoleId());
            super.update(nm);
        }
    }

    private void restoreProjectAssignments(Set<ProjecAssignmentBackup> pas, Map<Long, ProjectBean> ps, Map<String, UserBean> us) {
        for (ProjecAssignmentBackup p : pas) {
            ProjectBean pb = ps.get(p.getProjectId());
            UserBean u = us.get(p.getUserId());
            if (pb.getAssignedUsers() == null) pb.setAssignedUsers(new ArrayList<>());
            pb.getAssignedUsers().add(u);
            super.update(pb);
        }
    }

    private void restoreWorklogs(Set<WorklogBackup> worklogs, Map<Long, ActivityBean> sortedActivities) {
        for (WorklogBackup w : worklogs) {
            WorklogBean nw = new WorklogBean();
            nw.setId(w.getId());
            nw.setUserId(w.getUserId());
            nw.setActivity(sortedActivities.get(w.getActivityId()));
            nw.setDay(w.getDay());
            nw.setLoggedMinutes(w.getLoggedMinutes());
            nw.setConfirmed(w.getConfirmed());
            super.update(nw);
        }
    }
}