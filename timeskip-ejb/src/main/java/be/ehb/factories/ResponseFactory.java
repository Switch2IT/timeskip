package be.ehb.factories;

import be.ehb.entities.mail.MailTemplateBean;
import be.ehb.entities.organizations.MembershipBean;
import be.ehb.entities.organizations.OrganizationBean;
import be.ehb.entities.projects.ActivityBean;
import be.ehb.entities.projects.ProjectBean;
import be.ehb.entities.projects.WorklogBean;
import be.ehb.entities.security.RoleBean;
import be.ehb.entities.users.PaygradeBean;
import be.ehb.entities.users.UserBean;
import be.ehb.model.responses.*;
import be.ehb.utils.DateUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDate;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public class ResponseFactory {

    public static Response buildResponse(Response.Status httpCode, String headerName, String headerValue, Object entity) {
        Response.ResponseBuilder builder = Response.status(httpCode.getStatusCode());
        if (StringUtils.isNotEmpty(headerName) && StringUtils.isNotEmpty(headerValue)) {
            builder.header(headerName, headerValue);
        }
        if (entity != null) {
            builder.entity(entity);
        }
        return builder.type(MediaType.APPLICATION_JSON_TYPE).build();
    }

    public static Response buildResponse(Response.Status httpCode) {
        return buildResponse(httpCode, null, null, null);
    }

    public static Response buildResponse(Response.Status httpCode, Object entity) {
        return buildResponse(httpCode, null, null, entity);
    }

    public static UserResponse createUserResponse(UserBean user) {
        UserResponse rval = null;
        if (user != null) {
            rval = new UserResponse();
            rval.setId(user.getId());
            rval.setFirstName(user.getFirstName());
            rval.setEmail(user.getEmail());
            rval.setLastName(user.getLastName());
            rval.setDefaultHoursPerDay(user.getDefaultHoursPerDay());
            rval.setWorkDays(user.getWorkdays());
            rval.setMemberships(createMembershipResponses(user.getMemberships()));
            rval.setAdmin(user.getAdmin());
            rval.setPaygrade(createPaygradeResponse(user.getPaygrade()));
        }
        return rval;
    }

    public static MembershipResponse createMembershipResponse(MembershipBean membership) {
        MembershipResponse rval = null;
        if (membership != null) {
            rval = new MembershipResponse();
            rval.setOrganizationId(membership.getOrganizationId());
            rval.setRole(membership.getRoleId());
        }
        return rval;
    }

    public static RoleResponse createRoleResponse(RoleBean role) {
        RoleResponse rval = null;
        if (role != null) {
            rval = new RoleResponse();
            rval.setId(role.getId());
            rval.setName(role.getName());
            rval.setDescription(role.getDescription());
            rval.setAutoGrant(role.getAutoGrant());
        }
        return rval;
    }

    public static OrganizationResponse createOrganizationResponse(OrganizationBean organization) {
        OrganizationResponse rval = null;
        if (organization != null) {
            rval = new OrganizationResponse();
            rval.setId(organization.getId());
            rval.setName(organization.getName());
            rval.setDescription(organization.getDescription());
        }
        return rval;
    }

    public static ProjectResponse createProjectResponse(ProjectBean project) {
        ProjectResponse rval = null;
        if (project != null) {
            rval = new ProjectResponse();
            rval.setId(project.getId());
            rval.setName(project.getName());
            rval.setDescription(project.getDescription());
            rval.setAllowOvertime(project.getAllowOvertime());
            rval.setBillOvertime(project.getBillOvertime());
            rval.setOrganization(createOrganizationResponse(project.getOrganization()));
        }
        return rval;
    }

    public static ActivityResponse createActivityResponse(ActivityBean activity) {
        ActivityResponse rval = null;
        if (activity != null) {
            rval = new ActivityResponse();
            rval.setId(activity.getId());
            rval.setName(activity.getName());
            rval.setDescription(activity.getDescription());
            rval.setBillable(activity.getBillable());
            rval.setProject(createProjectResponse(activity.getProject()));
        }
        return rval;
    }

    public static WorklogResponse createWorklogResponse(WorklogBean worklog) {
        WorklogResponse rval = null;
        if (worklog != null) {
            rval = new WorklogResponse();
            rval.setId(worklog.getId());
            rval.setActivity(createActivityResponse(worklog.getActivity()));
            rval.setConfirmed(worklog.getConfirmed());
            rval.setDay(new LocalDate(worklog.getDay()));
            rval.setLoggedMinutes(worklog.getLoggedMinutes());
            rval.setUserId(worklog.getUserId());
        }
        return rval;
    }

    public static MailTemplateResponse createMailTemplateResponse(MailTemplateBean template) {
        MailTemplateResponse rval = null;
        if (template != null) {
            rval = new MailTemplateResponse();
            rval.setTopic(template.getId().toString());
            rval.setSubject(template.getSubject());
            rval.setContent(template.getContent());
        }
        return rval;
    }

    public static DayOfMonthlyReminderResponse createDayOfMonthlyReminderResponse(Integer dayOfMonthlyReminder, Boolean lastDayOfMonth) {
        DayOfMonthlyReminderResponse rval = null;
        if (dayOfMonthlyReminder != null) {
            rval = new DayOfMonthlyReminderResponse();
            rval.setDayOfMonthlyReminder(dayOfMonthlyReminder);
        }
        if (lastDayOfMonth != null) {
            if (rval == null) rval = new DayOfMonthlyReminderResponse();
            rval.setLastDayOfMonth(lastDayOfMonth);
        }
        return rval;
    }

    public static PaygradeResponse createPaygradeResponse(PaygradeBean paygrade) {
        PaygradeResponse rval = new PaygradeResponse();
        if (paygrade != null) {
            rval = new PaygradeResponse();
            rval.setId(paygrade.getId());
            rval.setName(paygrade.getName());
            rval.setDescription(paygrade.getDescription());
            rval.setHourlyRate(paygrade.getHourlyRate());
        }
        return rval;
    }

    public static WorkDayResponse createWorkDayResponse(LocalDate day, Long minutes) {
        WorkDayResponse rval = null;
        if (day != null && minutes != null) {
            rval = new WorkDayResponse();
            rval.setDay(DateUtils.convertDateToString(day));
            rval.setLoggedMinutes(minutes);
        }
        return rval;
    }

    public static UserWorkDayResponse createUserWorkDayResponse(UserBean user, List<WorkDayResponse> workdays) {
        UserWorkDayResponse rval = null;
        if (user != null && CollectionUtils.isNotEmpty(workdays)) {
            rval = new UserWorkDayResponse();
            rval.setUser(createUserResponse(user));
            rval.setWorkdays(workdays);
        }
        return rval;
    }

    public static ActivityLoggedTimeResponse createActivityLoggedTimeResponse(ActivityBean activity, Long sum) {
        ActivityLoggedTimeResponse rval = null;
        if (activity != null && sum != null) {
            rval = new ActivityLoggedTimeResponse();
            rval.setActivity(createActivityResponse(activity));
            rval.setTotalLoggedMinutes(sum);
        }
        return rval;
    }

    public static ProjectLoggedTimeResponse createProjectLoggedTimeResponse(ProjectBean project, List<ActivityLoggedTimeResponse> alts, Long sum) {
        ProjectLoggedTimeResponse rval = null;
        if (project != null && CollectionUtils.isNotEmpty(alts) && sum != null) {
            rval = new ProjectLoggedTimeResponse();
            rval.setProject(createProjectResponse(project));
            rval.setActivities(alts);
            rval.setTotalLoggedMinutes(sum);
        }
        return rval;
    }

    public static OrganizationLoggedTimeResponse createOrganizationLoggedTimeResponse(OrganizationBean o, List<ProjectLoggedTimeResponse> plts, Long sum) {
        OrganizationLoggedTimeResponse rval = null;
        if (o != null && CollectionUtils.isNotEmpty(plts) && sum != null) {
            rval = new OrganizationLoggedTimeResponse();
            rval.setOrganization(createOrganizationResponse(o));
            rval.setProjects(plts);
            rval.setTotalLoggedMinutes(sum);
        }
        return rval;
    }

    public static UserLoggedTimeReportResponse createUserLoggedTimeReportResponse(UserBean user, LoggedTimeReportResponse loggedTimeReport) {
        UserLoggedTimeReportResponse rval = null;
        if (user != null && loggedTimeReport != null) {
            rval = new UserLoggedTimeReportResponse();
            rval.setUser(createUserResponse(user));
            rval.setReport(loggedTimeReport);
        }
        return rval;
    }

    public static UserBillingResponse createUserBillingResponse(UserBean u, Long totalMinutes) {
        UserBillingResponse rval = null;
        if (u != null && totalMinutes != null) {
            rval = new UserBillingResponse();
            rval.setUser(createUserResponse(u));

            rval.setTotalBillableHours(new BigDecimal(totalMinutes.doubleValue() / 60).setScale(2, BigDecimal.ROUND_HALF_UP));
            rval.setTotalAmountDue(rval.getTotalBillableHours().multiply(new BigDecimal(u.getPaygrade().getHourlyRate().toString()).setScale(2, BigDecimal.ROUND_HALF_UP)));
        }
        return rval;
    }

    public static ActivityBillingResponse createActivityBillingResponse(ActivityBean a, List<UserBillingResponse> users, Optional<BigDecimal> totalHours, Optional<BigDecimal> totalAmount) {
        ActivityBillingResponse rval = null;
        if (a != null && CollectionUtils.isNotEmpty(users) && totalHours.isPresent() && totalAmount.isPresent()) {
            rval = new ActivityBillingResponse();
            rval.setActivity(createActivityResponse(a));
            rval.setTotalBillableHours(totalHours.get());
            rval.setTotalAmountDue(totalAmount.get());
            rval.setUsers(users);
        }
        return rval;
    }

    public static ProjectBillingResponse createProjectBillingResponse(ProjectBean p, List<ActivityBillingResponse> activities, Optional<BigDecimal> totalHours, Optional<BigDecimal> totalAmount) {
        ProjectBillingResponse rval = null;
        if (p != null && CollectionUtils.isNotEmpty(activities) && totalHours.isPresent() && totalAmount.isPresent()) {
            rval = new ProjectBillingResponse();
            rval.setProject(createProjectResponse(p));
            rval.setActivities(activities);
            rval.setTotalBillableHours(totalHours.get());
            rval.setTotalAmountDue(totalAmount.get());
        }
        return rval;
    }

    public static DayBillingResponse createDayBillingResponse(LocalDate d, List<ProjectBillingResponse> projects, Optional<BigDecimal> totalHours, Optional<BigDecimal> totalAmount) {
        DayBillingResponse rval = null;
        if (d != null && CollectionUtils.isNotEmpty(projects) && totalHours.isPresent() && totalAmount.isPresent()) {
            rval = new DayBillingResponse();
            rval.setDay(DateUtils.convertDateToString(d));
            rval.setProjects(projects);
            rval.setTotalBillableHours(totalHours.get());
            rval.setTotalAmountDue(totalAmount.get());
        }
        return rval;
    }

    public static OrganizationBillingResponse createOrganizationBillingResponse(OrganizationBean o, List<DayBillingResponse> days, Optional<BigDecimal> totalHours, Optional<BigDecimal> totalAmount) {
        OrganizationBillingResponse rval = null;
        if (o != null && CollectionUtils.isNotEmpty(days) && totalHours.isPresent() && totalAmount.isPresent()) {
            rval = new OrganizationBillingResponse();
            rval.setOrganization(createOrganizationResponse(o));
            rval.setDays(days);
            rval.setTotalBillableHours(totalHours.get());
            rval.setTotalAmountDue(totalAmount.get());
        }
        return rval;
    }

    public static BillingReportResponse createBillingReportResponse(List<OrganizationBillingResponse> obs, Optional<BigDecimal> totalHours, Optional<BigDecimal> totalAmount) {
        BillingReportResponse rval = null;
        if (CollectionUtils.isNotEmpty(obs) && totalHours.isPresent() && totalAmount.isPresent()) {
            rval = new BillingReportResponse();
            rval.setOrganizations(obs);
            rval.setTotalBillableHours(totalHours.get());
            rval.setTotalAmountDue(totalAmount.get());
        }
        return rval;
    }

    private static List<MembershipResponse> createMembershipResponses(List<MembershipBean> memberships) {
        List<MembershipResponse> rval = null;
        if (memberships != null) {
            rval = memberships.stream().map(ResponseFactory::createMembershipResponse).collect(Collectors.toList());
        }
        return rval;
    }
}