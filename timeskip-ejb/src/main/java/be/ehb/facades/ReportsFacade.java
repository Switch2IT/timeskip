package be.ehb.facades;

import be.ehb.configuration.IAppConfig;
import be.ehb.entities.organizations.OrganizationBean;
import be.ehb.entities.projects.ActivityBean;
import be.ehb.entities.projects.ProjectBean;
import be.ehb.entities.projects.WorklogBean;
import be.ehb.entities.users.UserBean;
import be.ehb.factories.ExceptionFactory;
import be.ehb.factories.ResponseFactory;
import be.ehb.i18n.Messages;
import be.ehb.model.responses.*;
import be.ehb.security.ISecurityContext;
import be.ehb.security.PermissionType;
import be.ehb.storage.IStorageService;
import be.ehb.utils.DateUtils;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import org.apache.commons.collections.CollectionUtils;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.util.*;
import java.util.stream.Collectors;

import static com.itextpdf.kernel.geom.PageSize.A4;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@Default
public class ReportsFacade implements IReportsFacade {

    private static final Logger log = LoggerFactory.getLogger(ReportsFacade.class);

    private static final String OVERTIME_KEY = "overtime";
    private static final String UNDERTIME_KEY = "undertime";
    private static final int FONT_SIZE = 6;

    @Inject
    private IStorageService storage;
    @Inject
    private ISecurityContext securityContext;
    @Inject
    private IAppConfig config;

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
            List<Date> dates = DateUtils.getDatesBetween(from, to);
            Map<UserBean, Map<LocalDate, Long>> sorted = getLoggedMinutesPerUserPerDay(organizationId, null, null, null, from, to);
            List<UserWorkDayResponse> userWorkDayResponses = new ArrayList<>();
            List<UserBean> orgUsers = storage.listUsers(organizationId, null, null, null, null, null);
            sorted.forEach((UserBean user, Map<LocalDate, Long> map) -> {
                if (user.getDefaultHoursPerDay() != null) {
                    List<WorkDayResponse> workdays = new ArrayList<>();
                    map.forEach((day, minutes) -> {
                        if (DateUtils.convertHoursToMinutes(user.getDefaultHoursPerDay()) > minutes) {
                            WorkDayResponse wd = ResponseFactory.createWorkDayResponse(day, minutes);
                            if (wd != null) workdays.add(wd);
                        }
                    });
                    //Check for the days an employee was supposed to be working, but didn't log any hours
                    dates.stream()
                            .map(LocalDate::new)
                            .filter(d -> !map.containsKey(d) && !user.getWorkdays().parallelStream().map(DayOfWeek::getValue).filter(dw -> dw == d.getDayOfWeek()).collect(Collectors.toList()).isEmpty())
                            .forEach(d -> {
                                WorkDayResponse wd = ResponseFactory.createWorkDayResponse(d, 0L);
                                if (wd != null) workdays.add(wd);
                            });

                    UserWorkDayResponse uwd = ResponseFactory.createUserWorkDayResponse(user, workdays);
                    if (uwd != null) userWorkDayResponses.add(uwd);
                }
            });
            orgUsers.parallelStream().filter(u -> !sorted.keySet().parallelStream().map(UserBean::getId).collect(Collectors.toList()).contains(u.getId())).forEach(u -> {
                if (CollectionUtils.isNotEmpty(u.getWorkdays())) {
                    List<WorkDayResponse> workdays = new ArrayList<>();
                    dates.stream()
                            .map(LocalDate::new)
                            .filter(d -> !u.getWorkdays().parallelStream().map(DayOfWeek::getValue).filter(dw -> dw == d.getDayOfWeek()).collect(Collectors.toList()).isEmpty())
                            .forEach(d -> {
                                WorkDayResponse wd = ResponseFactory.createWorkDayResponse(d, 0L);
                                if (wd != null) workdays.add(wd);
                            });
                    UserWorkDayResponse uwd = ResponseFactory.createUserWorkDayResponse(u, workdays);
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
            if (rval == null) return null;
            else return rval;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ExceptionFactory.systemErrorException(ex);
        }
    }

    // PDF Methods, not yet supported


    @Override
    public InputStream getPdfOvertimeReport(String organizationId, String from, String to) {
        OverUnderTimeReportResponse rep = getOvertimeReport(organizationId, from, to);
        return getPdfOverUnderTimeInternal(organizationId, rep, OVERTIME_KEY, from, to);
    }

    @Override
    public InputStream getPdfUndertimeReport(String organizationId, String from, String to) {
        OverUnderTimeReportResponse rep = getUndertimeReport(organizationId, from, to);
        return getPdfOverUnderTimeInternal(organizationId, rep, UNDERTIME_KEY, from, to);
    }

    @Override
    public InputStream getPdfBillingReport(String organizationId, Long projectId, Long activityId, String userId, String from, String to) {
        BillingReportResponse rep = getBillingReport(organizationId, projectId, activityId, userId, from, to);
        String cs = config.getCurrencySymbol();
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            if (rep != null) {
                Document doc = new Document(new PdfDocument(new PdfWriter(out)), A4).setFontSize(12);
                doc = addLogoToDocument(doc);
                doc.add(new Paragraph((Messages.i18n.format("invoice"))).setFontSize(24).setBold());
                doc.add(new Paragraph((Messages.i18n.format("invoiceDescription", from, to))).setFontSize(14).setItalic());

                Table outerTable = new Table(new float[]{2, 14});
                outerTable.setWidthPercent(100);
                outerTable.addHeaderCell(new Cell(1, 2).setBold().add(Messages.i18n.format("organization")).setFontSize(FONT_SIZE));

                rep.getOrganizations().forEach(ob -> {

                    Table dayTable = new Table(new float[]{3, 13});
                    dayTable.setWidthPercent(100);
                    dayTable.addHeaderCell(new Cell(1, 2).setBold().add(Messages.i18n.format("day")).setFontSize(FONT_SIZE));

                    ob.getDays().forEach(db -> {

                        Table projectTable = new Table(new float[]{5, 11});
                        projectTable.setWidthPercent(100);
                        projectTable.addHeaderCell(new Cell(1, 2).setBold().add(Messages.i18n.format("project")).setFontSize(FONT_SIZE));

                        db.getProjects().forEach(pb -> {

                            Table activityTable = new Table(new float[]{5, 11}).setMargin(0).setPadding(0);
                            activityTable.setWidthPercent(100);
                            activityTable.addHeaderCell(new Cell(1, 2).setBold().add(Messages.i18n.format("activity")).setFontSize(FONT_SIZE));

                            pb.getActivities().forEach(ab -> {
                                Table userTable = new Table(new float[]{6, 5, 5}).setMargin(0).setPadding(0);
                                userTable.setWidthPercent(100);
                                userTable.addHeaderCell(new Cell().setBold().add(Messages.i18n.format("employee")).setFontSize(FONT_SIZE));
                                userTable.addHeaderCell(new Cell().setBold().add(Messages.i18n.format("billableHours")).setFontSize(FONT_SIZE));
                                userTable.addHeaderCell(new Cell().setBold().add(Messages.i18n.format("amountDue")).setFontSize(FONT_SIZE));
                                ab.getUsers().forEach(ub -> {
                                    userTable.addCell(getSmallerFontCell(ub.getUser().getFirstName() + " " + ub.getUser().getLastName()));
                                    userTable.addCell(getSmallerFontCell(Messages.i18n.format("xhours", ub.getTotalBillableHours().setScale(1, RoundingMode.HALF_UP).toString())));
                                    userTable.addCell(getSmallerFontCell(Messages.i18n.format("xCurrency", ub.getTotalAmountDue().setScale(2, RoundingMode.HALF_UP).toString(), cs)));
                                });
                                userTable.addFooterCell(getSmallerFontCell(Messages.i18n.format("total")).setBold());
                                userTable.addFooterCell(getSmallerFontCell(Messages.i18n.format("xhours", ab.getTotalBillableHours().setScale(1, RoundingMode.HALF_UP).toString())));
                                userTable.addFooterCell(getSmallerFontCell(Messages.i18n.format("xCurrency", ab.getTotalAmountDue().setScale(2, RoundingMode.HALF_UP).toString(), cs)));
                                activityTable.addCell(getSmallerFontCell(ab.getActivity().getName()));
                                activityTable.addCell(getTableCell(userTable));
                            });
                            activityTable.addFooterCell(getSmallerFontCell(Messages.i18n.format("total")).setBold());
                            activityTable.addFooterCell(getTotalCell(pb.getTotalBillableHours(), pb.getTotalAmountDue()));
                            projectTable.addCell(getSmallerFontCell(pb.getProject().getName()));
                            projectTable.addCell(getTableCell(activityTable));
                        });
                        projectTable.addFooterCell(getSmallerFontCell(Messages.i18n.format("total")).setBold());
                        projectTable.addFooterCell(getTotalCell(db.getTotalBillableHours(), db.getTotalAmountDue()));
                        dayTable.addCell(getSmallerFontCell(db.getDay()));
                        dayTable.addCell(getTableCell(projectTable));
                    });
                    dayTable.addFooterCell(getSmallerFontCell(Messages.i18n.format("total")).setBold());
                    dayTable.addFooterCell(getTotalCell(ob.getTotalBillableHours(), ob.getTotalAmountDue()));
                    outerTable.addCell(getSmallerFontCell(ob.getOrganization().getName()));
                    outerTable.addCell(getTableCell(dayTable));
                });
                outerTable.addFooterCell(getSmallerFontCell(Messages.i18n.format("total")).setBold());
                outerTable.addFooterCell(getTotalCell(rep.getTotalBillableHours(), rep.getTotalAmountDue()));
                doc.add(outerTable);

                doc.close();
            }
            return new ByteArrayInputStream(out.toByteArray());
        } catch (Exception ex) {
            throw ExceptionFactory.systemErrorException(ex);
        }
    }

    @Override
    public InputStream getPdfLoggedTimeReport(String organizationId, Long projectId, Long activityId, String from, String to) {
        LoggedTimeReportResponse rep = getLoggedTimeReport(organizationId, projectId, activityId, from, to);
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            if (rep != null) {
                Document doc = new Document(new PdfDocument(new PdfWriter(out)), A4).setFontSize(12);
                doc = addLogoToDocument(doc);
                doc.add(new Paragraph((Messages.i18n.format("timelogReport"))).setFontSize(24).setBold());
                doc.add(new Paragraph((Messages.i18n.format("timelogDesc", from, to))).setFontSize(14).setItalic());

                Table outerTable = new Table(new float[]{2, 14});
                outerTable.setWidthPercent(100);
                outerTable.addHeaderCell(new Cell(1, 2).setBold().add(Messages.i18n.format("organization")).setFontSize(FONT_SIZE));
                doc.add(populateLoggedTimeTable(outerTable, rep));
                doc.close();
            }
            return new ByteArrayInputStream(out.toByteArray());
        } catch (Exception ex) {
            throw ExceptionFactory.systemErrorException(ex);
        }
    }

    @Override
    public InputStream getPdfUserReport(String organizationId, Long projectId, Long activityId, String userId, String from, String to) {
        UserLoggedTimeReportResponse rep = getUserReport(organizationId, projectId, activityId, userId, from, to);
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            if (rep != null) {
                Document doc = new Document(new PdfDocument(new PdfWriter(out)), A4).setFontSize(12);
                doc = addLogoToDocument(doc);
                doc.add(new Paragraph((Messages.i18n.format("userTimelogReport"))).setFontSize(24).setBold());
                doc.add(new Paragraph((Messages.i18n.format("userTimelogDesc", rep.getUser().getFirstName(), rep.getUser().getLastName(), from, to))).setFontSize(10).setItalic());

                Table outerTable = new Table(new float[]{2, 14});
                outerTable.setWidthPercent(100);
                outerTable.addHeaderCell(new Cell(1, 2).setBold().add(Messages.i18n.format("organization")).setFontSize(FONT_SIZE));

                doc.add(populateLoggedTimeTable(outerTable, rep.getReport()));
                doc.close();
            }
            return new ByteArrayInputStream(out.toByteArray());
        } catch (Exception ex) {
            throw ExceptionFactory.systemErrorException(ex);
        }
    }

    @Override
    public InputStream getPdfCurrentUserReport(String organizationId, Long projectId, Long activityId, String from, String to) {
        return getPdfUserReport(organizationId, projectId, activityId, securityContext.getCurrentUser(), from, to);
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
            if (a.getBillable() != null && a.getBillable()) {
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

    private InputStream getPdfOverUnderTimeInternal(String organizationId, OverUnderTimeReportResponse resp, String overUnderKey, String from, String to) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        if (resp != null) {
            try {
                Document doc = new Document(new PdfDocument(new PdfWriter(out)), A4);
                doc = addLogoToDocument(doc);
                doc.add(new Paragraph((Messages.i18n.format("organizationTitle", storage.getOrganization(organizationId).getName()))).setFontSize(24).setBold());
                doc.add(new Paragraph((Messages.i18n.format(overUnderKey, from, to))).setFontSize(14).setItalic());
                Table table = new Table(new float[]{5, 11});
                table.setWidthPercent(100);
                table.addHeaderCell(new Cell(1, 2).setBold().add(Messages.i18n.format("employee"))).setFontSize(FONT_SIZE);
                resp.getUserWorkdays().forEach(uwd -> {
                    Table innerTable = new Table(new float[]{5, 11});
                    innerTable.setWidthPercent(100);
                    innerTable.addHeaderCell(Messages.i18n.format("day")).setFontSize(FONT_SIZE);
                    innerTable.addHeaderCell(Messages.i18n.format("loggedtime")).setFontSize(FONT_SIZE);
                    uwd.getWorkdays().forEach(wd -> {
                        innerTable.addCell(getSmallerFontCell(wd.getDay()));
                        innerTable.addCell(getSmallerFontCell(Messages.i18n.format("xhours", DateUtils.convertMinutesToHours(wd.getLoggedMinutes()).toString())));
                    });
                    table.addCell(getSmallerFontCell(uwd.getUser().getFirstName() + " " + uwd.getUser().getLastName()));
                    table.addCell(getTableCell(innerTable));
                });
                doc.add(table);
                doc.close();
            } catch (Exception ex) {
                ex.printStackTrace();
                throw ExceptionFactory.systemErrorException(ex);
            }
        }
        return new ByteArrayInputStream(out.toByteArray());
    }

    private Table populateLoggedTimeTable(Table outerTable, LoggedTimeReportResponse rep) {
        rep.getOrganizations().forEach(o -> {

            Table projectTable = new Table(new float[]{5, 11});
            projectTable.setWidthPercent(100);
            projectTable.addHeaderCell(new Cell(1, 2).setBold().add(Messages.i18n.format("project")).setFontSize(FONT_SIZE));

            o.getProjects().forEach(p -> {

                Table activityTable = new Table(new float[]{5, 11}).setMargin(0).setPadding(0);
                activityTable.setWidthPercent(100);
                activityTable.addHeaderCell(new Cell(1, 2).setBold().add(Messages.i18n.format("activity")).setFontSize(FONT_SIZE));

                p.getActivities().forEach(a -> {
                    activityTable.addCell(getSmallerFontCell(a.getActivity().getName()));
                    activityTable.addCell(getSmallerFontCell(Messages.i18n.format("xhours", DateUtils.convertMinutesToHours(a.getTotalLoggedMinutes()))));
                });
                projectTable.addCell(getSmallerFontCell(p.getProject().getName()));
                projectTable.addCell(getTableCell(activityTable));
                projectTable.addFooterCell(getSmallerFontCell(Messages.i18n.format("total")).setBold());
                projectTable.addFooterCell(getSmallerFontCell(Messages.i18n.format("xhours", DateUtils.convertMinutesToHours(p.getTotalLoggedMinutes()))));
            });
            outerTable.addCell(getSmallerFontCell(o.getOrganization().getName()));
            outerTable.addCell(getTableCell(projectTable));
            outerTable.addFooterCell(getSmallerFontCell(Messages.i18n.format("total")).setBold());
            outerTable.addFooterCell(getSmallerFontCell(Messages.i18n.format("xhours", DateUtils.convertMinutesToHours(o.getTotalLoggedMinutes()))));
        });
        return outerTable;
    }

    private Cell getTableCell(Table table) {
        return new Cell().add(table).setPadding(0).setMargin(0);
    }

    private Cell getSmallerFontCell(String content) {
        return new Cell().add(content).setFontSize(FONT_SIZE);
    }

    private Cell getTotalCell(BigDecimal hours, BigDecimal amount) {
        Cell cell = new Cell().setFontSize(FONT_SIZE).setPadding(0).setMargin(0);
        Table total = new Table(new float[]{4, 4});
        total.setWidthPercent(100);
        total.addCell(Messages.i18n.format("xhours", hours.setScale(1, RoundingMode.HALF_UP).toString()));
        total.addCell(Messages.i18n.format("xCurrency", amount.setScale(2, RoundingMode.HALF_UP).toString(), config.getCurrencySymbol()));
        cell.add(total);
        return cell;
    }

    private Document addLogoToDocument(Document document) {
        Image img = null;
        try {
            img = new Image(ImageDataFactory.create(config.getPdfLogoLocation())).setWidthPercent(20);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (img != null) {
            document.add(img);
        }
        return document;
    }

}