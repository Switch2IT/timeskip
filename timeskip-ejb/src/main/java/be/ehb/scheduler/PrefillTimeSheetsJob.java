package be.ehb.scheduler;

import be.ehb.entities.projects.ActivityBean;
import be.ehb.entities.projects.WorklogBean;
import be.ehb.entities.users.UserBean;
import be.ehb.exceptions.MissingScheduledJobDependenciesException;
import be.ehb.facades.IOrganizationFacade;
import be.ehb.factories.ExceptionFactory;
import be.ehb.mail.IMailService;
import be.ehb.model.mail.PrefillTimeSheetMailBean;
import be.ehb.storage.IStorageService;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author Patrick Van den Bussche/Guillaume Vandecasteele
 * @since 2017
 */
public class PrefillTimeSheetsJob implements Job {

    public static final String MAIL_SERVICE_CONTEXT_KEY = "mailServiceContext";
    public static final String STORAGE_SERVICE_CONTEXT_KEY = "storageServiceContext";
    public static final String ORGANIZATION_FACADE_CONTEXT_KEY = "organizationFacadeContext";

    private static final Logger log = LoggerFactory.getLogger(PrefillTimeSheetsJob.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        SchedulerContext schedulerContext;
        try {
            schedulerContext = context.getScheduler().getContext();

            if (schedulerContext == null
                    || !schedulerContext.containsKey(MAIL_SERVICE_CONTEXT_KEY)
                    || !schedulerContext.containsKey(STORAGE_SERVICE_CONTEXT_KEY)
                    || !schedulerContext.containsKey(ORGANIZATION_FACADE_CONTEXT_KEY)) {
                throw ExceptionFactory.missingScheduledJobDependenciesException(PrefillTimeSheetsJob.class.getSimpleName());
            }

            IOrganizationFacade orgFacade = (IOrganizationFacade) schedulerContext.get(ORGANIZATION_FACADE_CONTEXT_KEY);
            IStorageService storage = (IStorageService) schedulerContext.get(STORAGE_SERVICE_CONTEXT_KEY);
            IMailService mailService = (IMailService) schedulerContext.get(MAIL_SERVICE_CONTEXT_KEY);

            if (orgFacade == null || storage == null || mailService == null) {
                throw ExceptionFactory.missingScheduledJobDependenciesException(PrefillTimeSheetsJob.class.getSimpleName());
            }

            List<UserBean> usersList = storage.listUsers(null, null, null, null, null, null);

            if (!usersList.isEmpty()) {

                Map<DayOfWeek, LocalDate> dayMap = new HashMap<>();
                LocalDate firstDate = LocalDate.now().with(DayOfWeek.MONDAY);
                for (int i = 0; i < 7; i++) {
                    dayMap.put(DayOfWeek.of(i + 1), firstDate.plusDays((long) i));
                }


                usersList.forEach(user -> {
                    ActivityBean userDefaultActivity = storage.getActivity(user.getDefaultActivity());
                    PrefillTimeSheetMailBean prefill = new PrefillTimeSheetMailBean();

                    prefill.setUserName(user.getFirstName());
                    prefill.setTo(user.getEmail());
                    StringBuilder worklogList = new StringBuilder("<table bgcolor=\"#f6f6f6\">");
                    worklogList.append("<tr style=\"clear: both !important; display: block !important; Margin: 0 auto !important; max-width: 600px !important\"><td style=\"padding-right:20px\"><b><i>Date</i></b></td><td style=\"padding-right:20px\"><b><i>Hours pre filled</i></b></td><td><b><i>Project/Action</i></b></td></tr>");

                    Set<DayOfWeek> sDayOfWeeks = new TreeSet<>();
                    sDayOfWeeks.addAll(user.getWorkdays());
                    sDayOfWeeks.forEach(dayOfWeek -> {
                        Date searchDate = Date.from(dayMap.get(dayOfWeek).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
                        WorklogBean worklogBean = storage.searchWorklogsByIdAndDay(user.getId(), searchDate);
                        BigDecimal defaultHoursPerDay = new BigDecimal(user.getDefaultHoursPerDay()).setScale(2, BigDecimal.ROUND_HALF_UP);
                        if (worklogBean.getLoggedMinutes() == null) {
                            orgFacade.createPrefillWorklog(createWorklogBean(user, searchDate, defaultHoursPerDay, userDefaultActivity));
                        } else {
                            BigDecimal loggedHoursPerDay = new BigDecimal(worklogBean.getLoggedMinutes().doubleValue() / 60).setScale(2, BigDecimal.ROUND_HALF_UP);
                            if (loggedHoursPerDay.compareTo(defaultHoursPerDay) >= 0) {
                                defaultHoursPerDay = new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP);
                            } else {
                                defaultHoursPerDay = defaultHoursPerDay.subtract(loggedHoursPerDay);
                                orgFacade.createPrefillWorklog(createWorklogBean(user, searchDate, defaultHoursPerDay, userDefaultActivity));
                            }
                        }
                        worklogList
                                .append("<tr style=\"clear: both !important; display: block !important; Margin: 0 auto !important; max-width: 600px !important\"><td style=\"padding-right:20px\">")
                                .append(dayMap.get(dayOfWeek).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                                .append("</td><td style=\"padding-right:20px\"><b>")
                                .append(defaultHoursPerDay)
                                .append("</b></td><td>")
                                .append(defaultHoursPerDay.compareTo(BigDecimal.ZERO) > 0 ? userDefaultActivity.getDescription()
                                        : "dna - already project/actions filled in")
                                .append("</td></tr>");
                    });
                    worklogList.append("</table>");
                    prefill.setPrefillWorklog(worklogList.toString());
                    mailService.sendPrefillTimeSheet(prefill);
                });
            }
        } catch (SchedulerException | MissingScheduledJobDependenciesException ex) {
            log.error("Unable to execute scheduled timesheets prefill job: {}", ex);
        }
    }

    private WorklogBean createWorklogBean(UserBean userBean, Date searchDate, BigDecimal defaultHoursPerDay, ActivityBean activity) {
        WorklogBean wlb = new WorklogBean();
        wlb.setId(null);
        wlb.setUserId(userBean.getId());
        wlb.setDay(searchDate);
        wlb.setLoggedMinutes(defaultHoursPerDay.longValue() * 60);
        wlb.setConfirmed(false);
        wlb.setActivity(activity);
        return wlb;
    }
}
