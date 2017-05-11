package be.ehb.facades;

import be.ehb.entities.organizations.OrganizationBean;
import be.ehb.entities.projects.ActivityBean;
import be.ehb.entities.projects.ProjectBean;
import be.ehb.entities.projects.WorklogBean;
import be.ehb.entities.users.UserBean;
import be.ehb.factories.ExceptionFactory;
import be.ehb.factories.ResponseFactory;
import be.ehb.model.responses.*;
import be.ehb.security.ISecurityContext;
import be.ehb.security.PermissionType;
import be.ehb.storage.IStorageService;
import be.ehb.utils.DateUtils;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@Default
public class ReportsFacade implements IReportsFacade {

    private static final Logger log = LoggerFactory.getLogger(ReportsFacade.class);

    @Inject
    private IStorageService storage;
    @Inject
    private ISecurityContext securityContext;

    @Override
    public OverUnderTimeReportResponse getOvertimeReport(String organizationId, String from, String to) {
        OverUnderTimeReportResponse rval = new OverUnderTimeReportResponse();
        Map<UserBean, Map<LocalDate, Long>> sorted = getLoggedMinutesPerUserPerDay(organizationId, null, null, null, from, to);
        List<UserWorkDayResponse> userWorkDayResponses = new ArrayList<>();
        sorted.forEach((user, map) -> {
            if (user.getDefaultHoursPerDay() != null) {
                List<WorkDayResponse> workdays = new ArrayList<>();
                map.forEach((day, minutes) -> {
                    if (DateUtils.convertHoursToMinutes(user.getDefaultHoursPerDay()) < minutes) {
                        WorkDayResponse wd = ResponseFactory.createWorkDayResponse(day, minutes);
                        if (wd != null) workdays.add(wd);
                    }
                });
                UserWorkDayResponse uwd = ResponseFactory.createUserWorkDayResponse(user, workdays);
                if (uwd != null) userWorkDayResponses.add(uwd);
            }
        });
        rval.setUserWorkdays(userWorkDayResponses);
        return rval;
    }

    @Override
    public OverUnderTimeReportResponse getUndertimeReport(String organizationId, String from, String to) {
        try {
            OverUnderTimeReportResponse rval = new OverUnderTimeReportResponse();
            Map<UserBean, Map<LocalDate, Long>> sorted = getLoggedMinutesPerUserPerDay(organizationId, null, null, null, from, to);
            Map<UserBean, Map<LocalDate, Long>> filteredData = new HashMap<>();
            List<UserWorkDayResponse> userWorkDayResponses = new ArrayList<>();
            sorted.forEach((user, map) -> {
                if (user.getDefaultHoursPerDay() != null) {
                    List<WorkDayResponse> workdays = new ArrayList<>();
                    map.forEach((day, minutes) -> {
                        if (DateUtils.convertHoursToMinutes(user.getDefaultHoursPerDay()) > minutes) {
                            WorkDayResponse wd = ResponseFactory.createWorkDayResponse(day, minutes);
                            if (wd != null) workdays.add(wd);
                        }
                    });
                    UserWorkDayResponse uwd = ResponseFactory.createUserWorkDayResponse(user, workdays);
                    if (uwd != null) userWorkDayResponses.add(uwd);
                }
            });
            rval.setUserWorkdays(userWorkDayResponses);
            return rval;
        } catch (Exception ex) {
            throw ExceptionFactory.systemErrorException(ex);
        }
    }

    @Override
    public LoggedTimeReportResponse getLoggedTimeReport(String organizationId, Long projectId, Long activityId, String from, String to) {
        try {
            return getLoggedTimeReportInternal(organizationId, projectId, activityId, null, from, to);
        } catch (Exception ex) {
            throw ExceptionFactory.systemErrorException(ex);
        }
    }

    @Override
    public UserLoggedTimeReportResponse getUserReport(String organizationId, Long projectId, Long activityId, String userId, String from, String to) {
        try {
            UserBean user = storage.getUser(userId);
            return ResponseFactory.createUserLoggedTimeReportResponse(user, getLoggedTimeReportInternal(organizationId, projectId, activityId, userId, from, to));
        } catch (Exception ex) {
            throw ExceptionFactory.systemErrorException(ex);
        }
    }

    @Override
    public UserLoggedTimeReportResponse getCurrentUserReport(String organizationId, Long projectId, Long activityId, String from, String to) {
        return getUserReport(organizationId, projectId, activityId, securityContext.getCurrentUser(), from, to);
    }

    @Override
    public BillingReportResponse getBillingReport(String organizationId, Long projectId, Long activityId, String userId, String from, String to) {
        try {

            List<OrganizationBillingResponse> obr = new ArrayList<>();
            sortWorklogsForBilling(organizationId, projectId, activityId, userId, from, to).forEach((o, dpauws) -> {
                List<DayBillingResponse> dbr = new ArrayList<>();
                dpauws.forEach((d, pauws) -> {
                    List<ProjectBillingResponse> pbr = new ArrayList<>();
                    pauws.forEach((p, auws) -> {
                        List<ActivityBillingResponse> abr = new ArrayList<>();
                        auws.forEach((a, uws) -> {
                            List<UserBillingResponse> ubr = new ArrayList<>();
                            uws.forEach((u, ws) -> {
                                if (u.getPaygrade() != null) {
                                    Long totalMinutes = ws.parallelStream().mapToLong(WorklogBean::getLoggedMinutes).sum();
                                    UserBillingResponse ub = ResponseFactory.createUserBillingResponse(u, totalMinutes);
                                    if (ub != null) ubr.add(ub);
                                }
                            });
                            Optional<BigDecimal> totalHours = ubr.parallelStream().map(UserBillingResponse::getTotalBillableHours).reduce(BigDecimal::add);
                            Optional<BigDecimal> amountDue = ubr.parallelStream().map(UserBillingResponse::getTotalAmountDue).reduce(BigDecimal::add);
                            ActivityBillingResponse ab = ResponseFactory.createActivityBillingResponse(a, ubr, totalHours, amountDue);
                            if (ab != null) abr.add(ab);
                        });
                        Optional<BigDecimal> totalHours = abr.parallelStream().map(ActivityBillingResponse::getTotalBillableHours).reduce(BigDecimal::add);
                        Optional<BigDecimal> totalAmount = abr.parallelStream().map(ActivityBillingResponse::getTotalAmountDue).reduce(BigDecimal::add);
                        ProjectBillingResponse pb = ResponseFactory.createProjectBillingResponse(p, abr, totalHours, totalAmount);
                        if (pb != null) pbr.add(pb);
                    });
                    Optional<BigDecimal> totalHours = pbr.parallelStream().map(ProjectBillingResponse::getTotalBillableHours).reduce(BigDecimal::add);
                    Optional<BigDecimal> totalAmount = pbr.parallelStream().map(ProjectBillingResponse::getTotalAmountDue).reduce(BigDecimal::add);
                    DayBillingResponse db = ResponseFactory.createDayBillingResponse(d, pbr, totalHours, totalAmount);
                    if (db != null) dbr.add(db);
                });
                Optional<BigDecimal> totalHours = dbr.parallelStream().map(DayBillingResponse::getTotalBillableHours).reduce(BigDecimal::add);
                Optional<BigDecimal> totalAmount = dbr.parallelStream().map(DayBillingResponse::getTotalAmountDue).reduce(BigDecimal::add);
                OrganizationBillingResponse ob = ResponseFactory.createOrganizationBillingResponse(o, dbr, totalHours, totalAmount);
                if (ob != null) obr.add(ob);
            });
            Optional<BigDecimal> totalHours = obr.parallelStream().map(OrganizationBillingResponse::getTotalBillableHours).reduce(BigDecimal::add);
            Optional<BigDecimal> totalAmount = obr.parallelStream().map(OrganizationBillingResponse::getTotalAmountDue).reduce(BigDecimal::add);
            BillingReportResponse rval = ResponseFactory.createBillingReportResponse(obr, totalHours, totalAmount);
            if (rval == null) return new BillingReportResponse();
            else return rval;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ExceptionFactory.systemErrorException(ex);
        }
    }

    // PDF Methods, not yet supported

    @Override
    public InputStream getPdfOvertimeReport(String organizationId, Long projectId, Long activityId, String from, String to) {
        throw new UnsupportedOperationException();
    }

    @Override
    public InputStream getPdfUndertimeReport(String organizationId, Long projectId, Long activityId, String from, String to) {
        throw new UnsupportedOperationException();
    }

    @Override
    public InputStream getPdfLoggedTimeReport(String organizationId, Long projectId, Long activityId, String from, String to) {
        throw new UnsupportedOperationException();
    }

    @Override
    public InputStream getPdfUserReport(String organizationId, Long projectId, Long activityId, String userId, String from, String to) {
        throw new UnsupportedOperationException();
    }

    @Override
    public InputStream getPdfCurrentUserReport(String organizationId, Long projectId, Long activityId, String from, String to) {
        throw new UnsupportedOperationException();
    }

    @Override
    public InputStream getPdfBillingReport(String organizationId, Long projectId, Long activityId, String from, String to) {
        throw new UnsupportedOperationException();
    }

    // PRIVATE METHODS

    private Map<UserBean, Map<LocalDate, Long>> getLoggedMinutesPerUserPerDay(String organizationId, Long projectId, Long activityId, String userId, String from, String to) {
        Map<UserBean, Map<LocalDate, Long>> sorted = new HashMap<>();
        Map<String, UserBean> sortedUsers = new HashMap<>();
        storage.searchWorklogs(organizationId, projectId, activityId, userId, DateUtils.getDatesBetween(from, to)).forEach(w -> {
            LocalDate d = new LocalDate(w.getDay());
            UserBean u;
            if (sortedUsers.containsKey(w.getUserId())) {
                u = sortedUsers.get(w.getUserId());
            } else {
                u = storage.getUser(w.getUserId());
                sortedUsers.put(w.getUserId(), u);
            }
            if (sorted.containsKey(u)) {
                Map<LocalDate, Long> dateMap = sorted.get(u);
                if (dateMap.containsKey(d)) {
                    dateMap.put(d, dateMap.get(d) + w.getLoggedMinutes());
                } else {
                    dateMap.put(d, w.getLoggedMinutes());
                }
            } else {
                Map<LocalDate, Long> dateMap = new HashMap<>();
                dateMap.put(d, w.getLoggedMinutes());
                sorted.put(u, dateMap);
            }
        });
        return sorted;
    }

    private LoggedTimeReportResponse getLoggedTimeReportInternal(String organizationId, Long projectId, Long activityId, String userId, String from, String to) {
        Map<OrganizationBean, Map<ProjectBean, Map<ActivityBean, List<WorklogBean>>>> sorted = new HashMap<>();

        storage.searchWorklogs(organizationId, projectId, activityId, userId, DateUtils.getDatesBetween(from, to)).forEach(w -> {
            ActivityBean a = w.getActivity();
            ProjectBean p = a.getProject();
            OrganizationBean o = p.getOrganization();
            // Check if the use is permitted to retrieve the information about the worklog
            if (securityContext.hasPermission(PermissionType.ORG_VIEW, organizationId)
                    && (securityContext.hasPermission(PermissionType.PROJECT_VIEW_ALL, organizationId)
                    || (securityContext.hasPermission(PermissionType.PROJECT_VIEW, organizationId) && !p.getAssignedUsers().parallelStream()
                    .filter(u -> u.getId().equals(securityContext.getCurrentUser())).collect(Collectors.toList()).isEmpty()))
                    && (securityContext.hasPermission(PermissionType.WORKLOG_VIEW_ALL, organizationId)
                    || (securityContext.hasPermission(PermissionType.WORKLOG_VIEW, organizationId) && w.getUserId().equals(securityContext.getCurrentUser())))) {
                if (sorted.containsKey(o)) {
                    if (sorted.get(o).containsKey(p)) {
                        if (sorted.get(o).get(p).containsKey(a)) {
                            sorted.get(o).get(p).get(a).add(w);
                        } else {
                            List<WorklogBean> ws = new ArrayList<>();
                            ws.add(w);
                            sorted.get(o).get(p).put(a, ws);
                        }
                    } else {
                        Map<ActivityBean, List<WorklogBean>> aws = new HashMap<>();
                        List<WorklogBean> ws = new ArrayList<>();
                        ws.add(w);
                        aws.put(a, ws);
                        sorted.get(o).put(p, aws);
                    }
                } else {
                    Map<ProjectBean, Map<ActivityBean, List<WorklogBean>>> paws = new HashMap<>();
                    Map<ActivityBean, List<WorklogBean>> aws = new HashMap<>();
                    List<WorklogBean> ws = new ArrayList<>();
                    ws.add(w);
                    aws.put(a, ws);
                    paws.put(p, aws);
                    sorted.put(o, paws);
                }
            }
        });
        List<OrganizationLoggedTimeResponse> olts = new ArrayList<>();
        sorted.forEach((o, ps) -> {
            List<ProjectLoggedTimeResponse> plts = new ArrayList<>();
            ps.forEach((p, as) -> {
                List<ActivityLoggedTimeResponse> alts = new ArrayList<>();
                as.forEach((a, ws) -> {
                    ActivityLoggedTimeResponse alt = ResponseFactory.createActivityLoggedTimeResponse(a, ws.parallelStream().mapToLong(WorklogBean::getLoggedMinutes).sum());
                    if (alt != null) alts.add(alt);
                });
                ProjectLoggedTimeResponse plt = ResponseFactory.createProjectLoggedTimeResponse(p, alts, alts.parallelStream().mapToLong(ActivityLoggedTimeResponse::getTotalLoggedMinutes).sum());
                if (plt != null) plts.add(plt);
            });
            OrganizationLoggedTimeResponse olt = ResponseFactory.createOrganizationLoggedTimeResponse(o, plts, plts.parallelStream().mapToLong(ProjectLoggedTimeResponse::getTotalLoggedMinutes).sum());
            if (olt != null) olts.add(olt);
        });
        LoggedTimeReportResponse rval = new LoggedTimeReportResponse();
        rval.setOrganizations(olts);
        return rval;
    }

    private Map<OrganizationBean, Map<LocalDate, Map<ProjectBean, Map<ActivityBean, Map<UserBean, List<WorklogBean>>>>>> sortWorklogsForBilling(String organizationId, Long projectId, Long activityId, String userId, String from, String to) {
        Map<OrganizationBean, Map<LocalDate, Map<ProjectBean, Map<ActivityBean, Map<UserBean, List<WorklogBean>>>>>> sorted = new HashMap<>();

        storage.searchWorklogs(organizationId, projectId, activityId, userId, DateUtils.getDatesBetween(from, to)).forEach(w -> {
            ActivityBean a = w.getActivity();
            if (a.getBillable()) {
                ProjectBean p = a.getProject();
                OrganizationBean o = p.getOrganization();
                UserBean u = storage.getUser(w.getUserId());
                LocalDate d = new LocalDate(w.getDay());
                if (sorted.containsKey(o)) {
                    if (sorted.get(o).containsKey(d)) {
                        if (sorted.get(o).get(d).containsKey(p)) {
                            if (sorted.get(o).get(d).get(p).containsKey(a)) {
                                if (sorted.get(o).get(d).get(p).get(a).containsKey(u)) {
                                    if (!p.getBillOvertime()) {
                                        Long alreadyLogged = sorted.get(o).get(d).get(p).get(a).get(u).parallelStream().mapToLong(WorklogBean::getLoggedMinutes).sum();
                                        if (alreadyLogged + w.getLoggedMinutes() > u.getDefaultHoursPerDay()) {
                                            w.setLoggedMinutes(alreadyLogged + w.getLoggedMinutes() - DateUtils.convertHoursToMinutes(u.getDefaultHoursPerDay()));
                                        }
                                    }
                                    sorted.get(o).get(d).get(p).get(a).get(u).add(w);
                                } else {
                                    List<WorklogBean> ws = new ArrayList<>();
                                    ws.add(w);
                                    sorted.get(o).get(d).get(p).get(a).put(u, ws);
                                }
                            } else {
                                Map<UserBean, List<WorklogBean>> uws = new HashMap<>();
                                List<WorklogBean> ws = new ArrayList<>();
                                ws.add(w);
                                uws.put(u, ws);
                                sorted.get(o).get(d).get(p).put(a, uws);
                            }
                        } else {
                            Map<ActivityBean, Map<UserBean, List<WorklogBean>>> auws = new HashMap<>();
                            Map<UserBean, List<WorklogBean>> uws = new HashMap<>();
                            List<WorklogBean> ws = new ArrayList<>();
                            ws.add(w);
                            uws.put(u, ws);
                            auws.put(a, uws);
                            sorted.get(o).get(d).put(p, auws);
                        }
                    } else {
                        Map<ProjectBean, Map<ActivityBean, Map<UserBean, List<WorklogBean>>>> pauws = new HashMap<>();
                        Map<ActivityBean, Map<UserBean, List<WorklogBean>>> auws = new HashMap<>();
                        Map<UserBean, List<WorklogBean>> uws = new HashMap<>();
                        List<WorklogBean> ws = new ArrayList<>();
                        ws.add(w);
                        uws.put(u, ws);
                        auws.put(a, uws);
                        pauws.put(p, auws);
                        sorted.get(o).put(d, pauws);
                    }
                } else {
                    Map<LocalDate, Map<ProjectBean, Map<ActivityBean, Map<UserBean, List<WorklogBean>>>>> dpauws = new HashMap<>();
                    Map<ProjectBean, Map<ActivityBean, Map<UserBean, List<WorklogBean>>>> pauws = new HashMap<>();
                    Map<ActivityBean, Map<UserBean, List<WorklogBean>>> auws = new HashMap<>();
                    Map<UserBean, List<WorklogBean>> uws = new HashMap<>();
                    List<WorklogBean> ws = new ArrayList<>();
                    ws.add(w);
                    uws.put(u, ws);
                    auws.put(a, uws);
                    pauws.put(p, auws);
                    dpauws.put(d, pauws);
                    sorted.put(o, dpauws);
                }
            }
        });
        return sorted;
    }
}