package be.ehb.utils;

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
import be.ehb.model.responses.BackUpResponse;
import com.google.common.base.Preconditions;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public final class BackupUtil {

    private BackupUtil() {
    }

    public static BackUpResponse createBackupResponse(List<OrganizationBean> organizations, List<ProjectBean> projects, List<ActivityBean> activities, List<PaygradeBean> paygrades, List<UserBean> users, List<RoleBean> roles, List<MembershipBean> memberships, List<MailTemplateBean> mailTemplates, List<ConfigBean> configurations, List<WorklogBean> worklogs) {
        BackUpResponse rval = null;

        // Organizations
        if (CollectionUtils.isNotEmpty(organizations)) {
            rval = new BackUpResponse();
            rval.setOrganizations(organizations.parallelStream().map(BackupUtil::createOrganizationBackup).filter(Objects::nonNull).collect(Collectors.toSet()));
        }

        // Projects
        Set<ProjecAssignmentBackup> assignments = new HashSet<>();
        if (CollectionUtils.isNotEmpty(projects)) {
            if (rval == null) {
                rval = new BackUpResponse();
            }
            rval.setProjects(projects.parallelStream().map(project -> {
                assignments.addAll(project.getAssignedUsers().parallelStream().map(user -> BackupUtil.createProjectAssignmentBackup(user.getId(), project.getId())).filter(Objects::nonNull).collect(Collectors.toSet()));
                return BackupUtil.createProjectBackup(project);
            }).filter(Objects::nonNull).collect(Collectors.toSet()));

        }

        // Activities
        if (CollectionUtils.isNotEmpty(activities)) {
            if (rval == null) {
                rval = new BackUpResponse();
            }
            rval.setActivities(activities.parallelStream().map(BackupUtil::createActivityBackup).filter(Objects::nonNull).collect(Collectors.toSet()));
        }

        // Paygrades
        if (CollectionUtils.isNotEmpty(paygrades)) {
            if (rval == null) {
                rval = new BackUpResponse();
            }
            rval.setPaygrades(paygrades.parallelStream().map(BackupUtil::createPaygradeBackup).filter(Objects::nonNull).collect(Collectors.toSet()));
        }

        // Users
        if (CollectionUtils.isNotEmpty(users)) {
            if (rval == null) {
                rval = new BackUpResponse();
            }
            rval.setUsers(users.parallelStream().map(BackupUtil::createUserBackup).filter(Objects::nonNull).collect(Collectors.toSet()));
        }

        if (!assignments.isEmpty()) {
            if (rval == null) {
                rval = new BackUpResponse();
            }
            rval.setAssignments(assignments);
        }

        // Roles
        if (CollectionUtils.isNotEmpty(roles)) {
            if (rval == null) {
                rval = new BackUpResponse();
            }
            rval.setRoles(roles.parallelStream().map(BackupUtil::createRoleBackup).filter(Objects::nonNull).collect(Collectors.toSet()));
        }

        // Memberships
        if (CollectionUtils.isNotEmpty(memberships)) {
            if (rval == null) {
                rval = new BackUpResponse();
            }
            rval.setMemberships(memberships.parallelStream().map(BackupUtil::createMembershipBackup).collect(Collectors.toSet()));
        }

        // Mail Templates
        if (CollectionUtils.isNotEmpty(mailTemplates)) {
            if (rval == null) {
                rval = new BackUpResponse();
            }
            rval.setMailTemplates(mailTemplates.parallelStream().map(BackupUtil::createMailTemplateBackup).filter(Objects::nonNull).collect(Collectors.toSet()));
        }

        // Configurations
        if (CollectionUtils.isNotEmpty(configurations)) {
            if (rval == null) {
                rval = new BackUpResponse();
            }
            rval.setConfigurations(configurations.parallelStream().map(BackupUtil::createConfigurationBackup).filter(Objects::nonNull).collect(Collectors.toSet()));
        }

        // Worklogs
        if (CollectionUtils.isNotEmpty(worklogs)) {
            if (rval == null) {
                rval = new BackUpResponse();
            }
            rval.setWorklogs(worklogs.parallelStream().map(BackupUtil::createWorklogBackup).filter(Objects::nonNull).collect(Collectors.toSet()));
        }
        if (rval != null) {
            rval.setDateOfBackup(new Date());
        }
        return rval;
    }

    private static ActivityBackup createActivityBackup(ActivityBean a) {
        ActivityBackup rval = null;
        if (a != null) {
            rval = new ActivityBackup();
            rval.setId(a.getId());
            rval.setName(a.getName());
            rval.setDescription(a.getDescription());
            rval.setBillable(a.getBillable());
            rval.setProjectId(a.getProject() == null ? null : a.getProject().getId());
        }
        return rval;
    }

    private static ProjectBackup createProjectBackup(ProjectBean p) {
        ProjectBackup rval = null;
        if (p != null) {
            rval = new ProjectBackup();
            rval.setId(p.getId());
            rval.setName(p.getName());
            rval.setDescription(p.getDescription());
            rval.setAllowOvertime(p.getAllowOvertime());
            rval.setBillOvertime(p.getBillOvertime());
            rval.setOrganizationId(p.getOrganization() == null ? null : p.getOrganization().getId());
        }
        return rval;
    }

    private static OrganizationBackup createOrganizationBackup(OrganizationBean o) {
        OrganizationBackup rval = null;
        if (o != null) {
            rval = new OrganizationBackup();
            rval.setId(o.getId());
            rval.setName(o.getName());
            rval.setDescription(o.getDescription());
        }
        return rval;
    }

    private static PaygradeBackup createPaygradeBackup(PaygradeBean p) {
        PaygradeBackup rval = null;
        if (p != null) {
            rval = new PaygradeBackup();
            rval.setId(p.getId());
            rval.setName(p.getName());
            rval.setDescription(p.getDescription());
            rval.setHourlyRate(p.getHourlyRate());
        }
        return rval;
    }

    private static UserBackup createUserBackup(UserBean u) {
        UserBackup rval = null;
        if (u != null) {
            rval = new UserBackup();
            rval.setId(u.getId());
            rval.setFirstName(u.getFirstName());
            rval.setLastName(u.getLastName());
            rval.setEmail(u.getEmail());
            rval.setAdmin(u.getAdmin());
            rval.setPaygradeId(u.getPaygrade() == null ? null : u.getPaygrade().getId());
            rval.setDefaultHoursPerDay(u.getDefaultHoursPerDay());
            rval.setDefaultActivityId(u.getDefaultActivity());
        }
        return rval;
    }

    private static ProjecAssignmentBackup createProjectAssignmentBackup(String userId, Long projectId) {
        ProjecAssignmentBackup rval = null;
        if (StringUtils.isNotEmpty(userId) && projectId != null) {
            rval = new ProjecAssignmentBackup();
            rval.setProjectId(projectId);
            rval.setUserId(userId);
        }
        return rval;
    }

    private static RoleBackup createRoleBackup(RoleBean r) {
        RoleBackup rval = null;
        if (r != null) {
            rval = new RoleBackup();
            rval.setId(r.getId());
            rval.setName(r.getName());
            rval.setDescription(r.getDescription());
            rval.setAutoGrant(r.getAutoGrant());
            rval.setPermissions(r.getPermissions() == null ? null : new HashSet<>(r.getPermissions()));
        }
        return rval;
    }

    private static MembershipBackup createMembershipBackup(MembershipBean m) {
        MembershipBackup rval = null;
        if (m != null) {
            rval = new MembershipBackup();
            rval.setId(m.getId());
            rval.setOrganizationId(m.getOrganizationId());
            rval.setRoleId(m.getRoleId());
            rval.setUserId(m.getUserId());
        }
        return rval;
    }

    private static MailTemplateBackup createMailTemplateBackup(MailTemplateBean m) {
        MailTemplateBackup rval = null;
        if (m != null) {
            rval = new MailTemplateBackup();
            rval.setId(m.getId());
            rval.setSubject(m.getSubject());
            rval.setContent(m.getContent());
        }
        return rval;
    }

    private static ConfigurationBackup createConfigurationBackup(ConfigBean c) {
        ConfigurationBackup rval = null;
        if (c != null) {
            rval = new ConfigurationBackup();
            rval.setId(c.getId());
            rval.setConfigPath(c.getConfigPath());
            rval.setDefaultConfig(c.getDefaultConfig());
            rval.setDayOfMonthlyReminderEmail(c.getDayOfMonthlyReminderEmail());
            rval.setLastDayOfMonth(c.getLastDayOfMonth());
        }
        return rval;
    }

    private static WorklogBackup createWorklogBackup(WorklogBean w) {
        WorklogBackup rval = null;
        if (w != null) {
            rval = new WorklogBackup();
            rval.setId(w.getId());
            rval.setActivityId(w.getActivity() == null ? null : w.getActivity().getId());
            rval.setUserId(w.getUserId());
            rval.setDay(w.getDay());
            rval.setLoggedMinutes(w.getLoggedMinutes());
            rval.setConfirmed(w.getConfirmed());
        }
        return rval;

    }

    public static void validateBackup(RestoreBackupRequest backup) {
        try {
            //Check mail templates
            Set<MailTemplateBackup> mailTemplates = backup.getMailTemplates();
            Preconditions.checkArgument(mailTemplates != null && mailTemplates.parallelStream().map(MailTemplateBackup::getId).filter(Objects::nonNull).collect(Collectors.toSet()).containsAll(Arrays.asList(MailTopic.values())), "Mail template data required and must contain all: " + Arrays.toString(MailTopic.values()));
            //Check configuration
            Set<ConfigurationBackup> configurations = backup.getConfigurations();
            Preconditions.checkArgument(CollectionUtils.isNotEmpty(configurations) && configurations.parallelStream().map(ConfigurationBackup::getDefaultConfig).filter(Objects::nonNull).collect(Collectors.toSet()).contains(true), "Configuration data required and one declared as default");

            Set<OrganizationBackup> organizations = backup.getOrganizations();
            Set<String> oIds = organizations != null ? organizations.parallelStream().map(OrganizationBackup::getId).filter(Objects::nonNull).collect(Collectors.toSet()) : new HashSet<>();
            Set<ProjectBackup> projects = backup.getProjects();
            Set<Long> pIds = projects != null ? projects.parallelStream().map(ProjectBackup::getId).filter(Objects::nonNull).collect(Collectors.toSet()) : new HashSet<>();
            Set<String> poIds = projects != null ? projects.parallelStream().map(ProjectBackup::getOrganizationId).filter(Objects::nonNull).collect(Collectors.toSet()) : new HashSet<>();
            Set<ActivityBackup> activities = backup.getActivities();
            Set<Long> aIds = projects != null ? activities.parallelStream().map(ActivityBackup::getId).filter(Objects::nonNull).collect(Collectors.toSet()) : new HashSet<>();
            Set<Long> payIds = backup.getPaygrades() != null ? backup.getPaygrades().parallelStream().map(PaygradeBackup::getId).filter(Objects::nonNull).collect(Collectors.toSet()) : new HashSet<>();
            Set<Long> apIds = projects != null ? activities.parallelStream().map(ActivityBackup::getProjectId).filter(Objects::nonNull).collect(Collectors.toSet()) : new HashSet<>();
            Set<UserBackup> users = backup.getUsers();
            Set<String> uIds = projects != null ? users.parallelStream().map(UserBackup::getId).filter(Objects::nonNull).collect(Collectors.toSet()) : new HashSet<>();
            Set<Long> uaIds = users != null ? users.parallelStream().map(UserBackup::getDefaultActivityId).filter(Objects::nonNull).collect(Collectors.toSet()) : new HashSet<>();
            Set<Long> upayIds = users != null ? users.parallelStream().map(UserBackup::getPaygradeId).filter(Objects::nonNull).collect(Collectors.toSet()) : new HashSet<>();
            Set<ProjecAssignmentBackup> assignments = backup.getAssignments();
            Set<String> upIds = assignments != null ? assignments.parallelStream().map(ProjecAssignmentBackup::getUserId).filter(Objects::nonNull).filter(Objects::nonNull).collect(Collectors.toSet()) : new HashSet<>();
            Set<Long> paIds = assignments != null ? assignments.parallelStream().map(ProjecAssignmentBackup::getProjectId).filter(Objects::nonNull).collect(Collectors.toSet()) : new HashSet<>();
            Set<RoleBackup> roles = backup.getRoles();
            Set<String> rIds = roles != null ? roles.parallelStream().map(RoleBackup::getId).filter(Objects::nonNull).collect(Collectors.toSet()) : new HashSet<>();
            Set<MembershipBackup> memberships = backup.getMemberships();
            Set<String> moIds = memberships != null ? memberships.parallelStream().map(MembershipBackup::getOrganizationId).filter(Objects::nonNull).collect(Collectors.toSet()) : new HashSet<>();
            Set<String> mrIds = memberships != null ? memberships.parallelStream().map(MembershipBackup::getRoleId).filter(Objects::nonNull).collect(Collectors.toSet()) : new HashSet<>();
            Set<String> muIds = memberships != null ? memberships.parallelStream().map(MembershipBackup::getUserId).filter(Objects::nonNull).collect(Collectors.toSet()) : new HashSet<>();
            Set<WorklogBackup> worklogs = backup.getWorklogs();
            Set<String> wuId = worklogs != null ? worklogs.parallelStream().map(WorklogBackup::getUserId).filter(Objects::nonNull).collect(Collectors.toSet()) : new HashSet<>();
            Set<Long> waId = worklogs != null ? worklogs.parallelStream().map(WorklogBackup::getActivityId).filter(Objects::nonNull).collect(Collectors.toSet()) : new HashSet<>();

            //Check activity dependencies
            Preconditions.checkArgument(pIds.containsAll(apIds), "Missing project data for activity restoration");
            //Check membership dependencies
            Preconditions.checkArgument(uIds.containsAll(muIds) && oIds.containsAll(moIds) && rIds.containsAll(mrIds), "Missing role, user or organization data for membership restoration");
            //Check project assignments dependencies
            Preconditions.checkArgument(uIds.containsAll(upIds) && pIds.containsAll(paIds), "Missing project or user data for project assignment restoration");
            //Check project dependencies
            Preconditions.checkArgument(oIds.containsAll(poIds), "Missing organization data for project restoration");
            //Check user dependencies
            Preconditions.checkArgument(payIds.containsAll(upayIds) && aIds.containsAll(uaIds), "Missing paygrade or project data for user restoration");
            //Check worklog dependencies
            Preconditions.checkArgument(aIds.containsAll(waId) && uIds.containsAll(wuId), "Missing user or activity data for worklog restoration");
        } catch (IllegalArgumentException ex) {
            throw ExceptionFactory.invalidBackupDataException(ex.getMessage());
        }
    }
}