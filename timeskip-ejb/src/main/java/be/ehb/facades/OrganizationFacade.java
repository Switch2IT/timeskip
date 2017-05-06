package be.ehb.facades;

import be.ehb.entities.organizations.MembershipBean;
import be.ehb.entities.organizations.OrganizationBean;
import be.ehb.entities.projects.ActivityBean;
import be.ehb.entities.projects.ProjectBean;
import be.ehb.entities.projects.WorklogBean;
import be.ehb.entities.security.RoleBean;
import be.ehb.entities.users.UserBean;
import be.ehb.exceptions.InvalidDateException;
import be.ehb.exceptions.OrganizationNotFoundException;
import be.ehb.exceptions.WorklogNotFoundException;
import be.ehb.factories.ExceptionFactory;
import be.ehb.factories.ResponseFactory;
import be.ehb.model.requests.*;
import be.ehb.model.responses.ActivityResponse;
import be.ehb.model.responses.OrganizationResponse;
import be.ehb.model.responses.ProjectResponse;
import be.ehb.model.responses.WorklogResponse;
import be.ehb.security.ISecurityContext;
import be.ehb.security.PermissionType;
import be.ehb.storage.IStorageService;
import be.ehb.utils.ConventionUtil;
import be.ehb.utils.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.*;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Date;
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
    private IUserFacade userFacade;
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
        if (request.getDescription() != null && !request.getDescription().equals(org.getDescription())) {
            org.setDescription(request.getDescription());
            changed = true;
        }
        if (StringUtils.isNotEmpty(request.getName()) && !request.getName().equals(org.getName())) {
            if (storage.findOrganizationByName(request.getName()) != null) {
                throw ExceptionFactory.organizationAlreadyExistsException(request.getName());
            }
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
        UserBean user = userFacade.get(securityContext.getCurrentUser());
        return storage.listProjects(organizationId).stream()
                .filter(project -> securityContext.hasPermission(PermissionType.PROJECT_VIEW_ALL, organizationId) || project.getAssignedUsers().contains(user))
                .map(ResponseFactory::createProjectResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ProjectResponse getProject(String organizationId, Long projectId) {
        ProjectBean project = storage.getProject(organizationId, projectId);
        if (!securityContext.hasPermission(PermissionType.PROJECT_VIEW_ALL, organizationId) || !project.getAssignedUsers().contains(userFacade.get(securityContext.getCurrentUser()))) {
            throw ExceptionFactory.unauthorizedException(projectId);
        }
        return ResponseFactory.createProjectResponse(project);
    }

    @Override
    public ProjectResponse createProject(String organizationId, NewProjectRequest request) {
        OrganizationBean org = storage.getOrganization(organizationId);
        if (storage.findProjectByName(organizationId, request.getName()) != null) {
            throw ExceptionFactory.projectAlreadyExistsException(request.getName());
        }
        ProjectBean newProject = new ProjectBean();
        newProject.setName(request.getName());
        newProject.setDescription(request.getDescription());
        newProject.setAllowOvertime(request.getAllowOvertime() == null ? true : request.getAllowOvertime());
        newProject.setBillOvertime(request.getBillOvertime() == null ? true : request.getAllowOvertime());
        newProject.setOrganization(org);

        return ResponseFactory.createProjectResponse(storage.createProject(newProject));
    }

    @Override
    public ProjectResponse updateProject(String organizationId, Long projectId, UpdateProjectRequest request) {
        ProjectBean project = storage.getProject(organizationId, projectId);
        boolean changed = false;
        if (request.getAllowOvertime() != null && request.getAllowOvertime() != project.getAllowOvertime()) {
            project.setAllowOvertime(request.getAllowOvertime());
            changed = true;
        }
        if (request.getBillOvertime() != null && request.getBillOvertime() != project.getBillOvertime()) {
            project.setBillOvertime(request.getBillOvertime());
            changed = true;
        }
        if (request.getDescription() != null && !request.getDescription().equals(project.getDescription())) {
            project.setDescription(request.getDescription());
            changed = true;
        }
        if (StringUtils.isNotEmpty(request.getName()) && !request.getName().equals(project.getName())) {
            if (storage.findProjectByName(organizationId, request.getName()) != null) {
                throw ExceptionFactory.projectAlreadyExistsException(request.getName());
            }
            project.setName(request.getName());
            changed = true;
        }
        if (changed) {
            return ResponseFactory.createProjectResponse(storage.updateProject(project));
        } else return null;
    }

    @Override
    public void deleteProject(String organizationId, Long projectId) {
        ProjectBean project = storage.getProject(organizationId, projectId);
        storage.deleteProject(project);
    }

    @Override
    public List<ActivityResponse> listProjectActivities(String organizationId, Long projectId) {
        return storage.listProjectActivities(organizationId, projectId).stream()
                .map(ResponseFactory::createActivityResponse)
                .collect(Collectors.toList());

    }

    @Override
    public ActivityResponse getActivity(String organizationId, Long projectId, Long activityId) {
        return ResponseFactory.createActivityResponse(storage.getActivity(organizationId, projectId, activityId));
    }

    @Override
    public ActivityResponse createActivity(String organizationId, Long projectID, NewActivityRequest request) {
        ProjectBean project = storage.getProject(organizationId, projectID);
        ActivityBean newActivity = new ActivityBean();
        ActivityBean existingActivity = storage.findActivityByName(organizationId, projectID, request.getName());
        if (existingActivity != null) {
            throw ExceptionFactory.activityAlreadyExistsException(request.getName());
        }
        newActivity.setName(request.getName());
        newActivity.setDescription(request.getDescription());
        newActivity.setBillable(request.getBillable() == null ? true : request.getBillable());
        newActivity.setProject(project);
        return ResponseFactory.createActivityResponse(storage.createActivity(newActivity));
    }

    @Override
    public ActivityResponse updateActivity(String organizationId, Long projectId, Long activityId, UpdateActivityRequest request) {
        ActivityBean activity = storage.getActivity(organizationId, projectId, activityId);
        boolean changed = false;
        if (request.getBillable() != null && request.getBillable() != activity.getBillable()) {
            activity.setBillable(request.getBillable());
            changed = true;
        }
        if (request.getDescription() != null && !request.getDescription().equals(activity.getDescription())) {
            activity.setDescription(request.getDescription());
            changed = true;
        }
        if (StringUtils.isNotEmpty(request.getName()) && !request.getName().equals(activity.getName())) {
            if (storage.findActivityByName(organizationId, projectId, request.getName()) != null) {
                throw ExceptionFactory.activityAlreadyExistsException(request.getName());
            }
            activity.setName(request.getName());
            changed = true;
        }
        if (changed) {
            return ResponseFactory.createActivityResponse(storage.updateActivity(activity));
        } else return null;
    }

    @Override
    public void deleteActivity(String organizationId, Long projectId, Long activityId) {
        ActivityBean activity = storage.getActivity(organizationId, projectId, activityId);
        storage.deleteActivity(activity);
    }

    @Override
    public List<WorklogResponse> listActivityWorklogs(String organizationId, Long projectId, Long activityId) {
        return storage.listActivityWorklogs(organizationId, projectId, activityId).stream()
                .filter(worklog -> securityContext.hasPermission(PermissionType.WORKLOG_VIEW_ALL, organizationId) || worklog.getUserId().equals(securityContext.getCurrentUser()))
                .map(ResponseFactory::createWorklogResponse)
                .collect(Collectors.toList());
    }

    @Override
    public WorklogResponse getWorklog(String organizationId, Long projectId, Long activityId, Long worklogId) {
        WorklogBean worklog = storage.getWorklog(organizationId, projectId, activityId, worklogId);
        if (!(worklog.getUserId().equals(securityContext.getCurrentUser()) || securityContext.hasPermission(PermissionType.WORKLOG_VIEW_ALL, organizationId))) {
            throw ExceptionFactory.unauthorizedException();
        }
        return ResponseFactory.createWorklogResponse(worklog);
    }

    @Override
    public WorklogResponse createWorkLog(String organizationId, Long projectId, Long activityId, NewWorklogRequest request) {
        ActivityBean activity = storage.getActivity(organizationId, projectId, activityId);
        Date day = DateUtils.convertStringToDate(request.getDay());
        UserBean user = storage.getUser(securityContext.getCurrentUser());
        //Check if the project allows overtime and if not, check if logging this work will exceed the limit
        if (!activity.getProject().getAllowOvertime()
                && storage.getUserLoggedMinutesForDay(user.getId(), day) + request.getLoggedMinutes() >
                DateUtils.convertHoursToMinutes(user.getDefaultHoursPerDay())) {
            throw ExceptionFactory.noOverTimeAllowedException(activity.getProject().getName());
        }
        WorklogBean newWorklog = new WorklogBean();
        newWorklog.setActivity(activity);
        newWorklog.setConfirmed(request.getConfirmed() == null ? false : request.getConfirmed());
        newWorklog.setDay(day);
        newWorklog.setLoggedMinutes(request.getLoggedMinutes());
        newWorklog.setUserId(user.getId());
        return ResponseFactory.createWorklogResponse(storage.createWorklog(newWorklog));
    }

    @Override
    public WorklogResponse updateWorklog(String organizationId, Long projectId, Long activityId, UpdateWorklogRequest request) {
        WorklogBean worklog = storage.getWorklog(organizationId, projectId, activityId, request.getId());
        if (!(worklog.getUserId().equals(securityContext.getCurrentUser()) || securityContext.hasPermission(PermissionType.WORKLOG_EDIT_ALL, organizationId))) {
            throw ExceptionFactory.unauthorizedException();
        }
        return ResponseFactory.createWorklogResponse(updateWorklog(worklog, userFacade.get(securityContext.getCurrentUser()), request.getDay(), request.getLoggedMinutes(), request
                .getConfirmed()));
    }

    @Override
    public void deleteWorklog(String organizationId, Long projectId, Long activityId, Long worklogId) {
        WorklogBean worklog = storage.getWorklog(organizationId, projectId, activityId, worklogId);
        if (!(worklog.getUserId().equals(securityContext.getCurrentUser()) || securityContext.hasPermission(PermissionType.WORKLOG_ADMIN_ALL, organizationId))) {
            throw ExceptionFactory.unauthorizedException(organizationId);
        }
        storage.deleteWorklog(worklog);
    }

    @Override
    public List<WorklogResponse> updateCurrentUserWorklogs(UpdateCurrentUserWorklogRequestList request) {
        List<WorklogResponse> rval = new ArrayList<>();
        UserBean user = userFacade.get(securityContext.getCurrentUser());
        return request.getUpdateCurrentUserWorklogRequests().stream().filter(req -> {
            //Check if the worklog exists, that the user has the right to edit them and that the user is assigned to the
            //worklog's activity's project
            WorklogBean worklogToUpdate = null;
            try {
                worklogToUpdate = storage.getWorklog(req.getId());
            } catch (WorklogNotFoundException ex) {
                //Do nothing
            }
            return worklogToUpdate != null
                    && securityContext.hasPermission(PermissionType.WORKLOG_EDIT, worklogToUpdate.getActivity().getProject().getOrganization().getId())
                    && worklogToUpdate.getActivity().getProject().getAssignedUsers().contains(user);
        }).map(req -> {
            WorklogBean worklog = storage.getWorklog(req.getId());
            return ResponseFactory.createWorklogResponse(updateWorklog(worklog, user, req.getDay(), req.getLoggedMinutes(), req.getConfirmed()));
        }).collect(Collectors.toList());
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    private WorklogBean updateWorklog(WorklogBean worklogToUpdate, UserBean user, String day, Long loggedMinutes, Boolean confirmed) {
        boolean changed = false;
        if (confirmed != null && confirmed.equals(worklogToUpdate.getConfirmed())) {
            worklogToUpdate.setConfirmed(confirmed);
            changed = true;
        }
        Date newDate;
        try {
            if (StringUtils.isNotEmpty(day)) newDate = DateUtils.convertStringToDate(day);
            else newDate = null;
        } catch (InvalidDateException ex) {
            newDate = null;
        }
        if (newDate != null && newDate.equals(worklogToUpdate.getDay())) {
            worklogToUpdate.setDay(newDate);
            changed = true;
        }
        if (loggedMinutes != null && !loggedMinutes.equals(worklogToUpdate.getLoggedMinutes())) {
            //Check if the changed minutes make
            if (!worklogToUpdate.getActivity().getProject().getAllowOvertime()
                    //Remove the current worklog's minutes before comparing it with the user's default hours
                    && storage.getUserLoggedMinutesForDay(user.getId(), worklogToUpdate.getDay()) + loggedMinutes - worklogToUpdate.getLoggedMinutes() >
                    DateUtils.convertHoursToMinutes(user.getDefaultHoursPerDay())) {
                throw ExceptionFactory.noOverTimeAllowedException(worklogToUpdate.getActivity().getProject().getName());
            }
            worklogToUpdate.setLoggedMinutes(loggedMinutes);
            changed = true;
        }
        if (changed) {
            return storage.updateWorklog(worklogToUpdate);
        } else return worklogToUpdate;
    }
}