package be.ehb.scheduler;

import be.ehb.entities.projects.WorklogBean;
import be.ehb.entities.users.UserBean;
import be.ehb.facades.IOrganizationFacade;
import be.ehb.mail.IMailService;
import be.ehb.model.mail.PrefillTimeSheetMailBean;
import be.ehb.storage.IStorageService;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

import java.time.DayOfWeek;
import java.util.List;

/**
 * @author Patrick Van den Bussche
 * @since 2017
 */

public class PrefillTimeSheetsJobContext {

        private static final Logger log = LoggerFactory.getLogger(PrefillTimeSheetsJobContext.class);

        private IMailService mailService;
        private IStorageService iss;
        private IOrganizationFacade iof;

        void setMailService(IMailService mailService) {
            this.mailService = mailService;
        }

        void setiof(IOrganizationFacade iof) {
            this.iof = iof;
        }

        void setIss(IStorageService iss) {
        this.iss = iss;
    }

        void execute() throws JobExecutionException {

            List<UserBean> usersList= iss.listUsers(null, null,null,null,null,null);

            if (!usersList.isEmpty()) {
                Map<DayOfWeek,LocalDate > dayMap = new HashMap<>();
                LocalDate firstDate = LocalDate.now().with(DayOfWeek.MONDAY);
                for (int i=0;i<7;i++) {
                    dayMap.put(DayOfWeek.of(i+1),firstDate.plusDays((long)i));
                }
                Map<String, List<UserBean>> sortedMap = new HashMap<>();
                usersList.forEach(ul -> {
                    if (sortedMap.containsKey(ul.getId())) {
                        sortedMap.get(ul.getId()).add(ul);
                    } else {
                        List<UserBean> list = new ArrayList<>();
                        list.add(ul);
                        sortedMap.put(ul.getId(), list);
                    }
                });
                sortedMap.forEach((String key, List<UserBean> value) -> {
                    PrefillTimeSheetMailBean prefill = new PrefillTimeSheetMailBean();
                    UserBean first = value.get(0);
                    prefill.setUserName(first.getFirstName());
                    prefill.setTo(first.getEmail());
                    StringBuilder worklogList = new StringBuilder("<table bgcolor=\"#f6f6f6\">");
                    worklogList.append("<tr style=\"clear: both !important; display: block !important; Margin: 0 auto !important; max-width: 600px !important\">")
                            .append("<td style=\"padding-right:20px\">")
                            .append("<b><i>Date</i></b>")
                            .append("</td>")
                            .append("<td style=\"padding-right:20px\">")
                            .append("<b><i>Hours pre filled</i></b>")
                            .append("</td>")
                            .append("<td>")
                            .append("<b><i>Project/Action</i></b>")
                            .append("</td>")
                            .append("</tr>");
                    value.forEach((UserBean entry) -> {
                            Set<DayOfWeek> sDayOfWeeks = new TreeSet<>();
                            entry.getWorkdays().forEach(dayOfWeek -> sDayOfWeeks.add(dayOfWeek));
                            sDayOfWeeks.forEach(dayOfWeek -> {
                                Date searchDate = Date.from(dayMap.get(dayOfWeek).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
                                WorklogBean worklogBean= iss.searchWorklogsByIdAndDay(entry.getId(), searchDate);
                                BigDecimal defaultHoursPerDay = new BigDecimal(entry.getDefaultHoursPerDay()).setScale(2);
                                if (worklogBean.getLoggedMinutes() != null) {
                                    BigDecimal loggedHoursPerDay = new BigDecimal(worklogBean.getLoggedMinutes().doubleValue() / 60).setScale(2, BigDecimal.ROUND_HALF_UP);
                                    if (loggedHoursPerDay.compareTo(defaultHoursPerDay) >= 0) {
                                        defaultHoursPerDay = new BigDecimal(0).setScale(2);
                                    } else {
                                        defaultHoursPerDay = defaultHoursPerDay.subtract(loggedHoursPerDay);
                                        this.createWorklogBean(entry, searchDate, defaultHoursPerDay);
                                    }
                                } else {
                                    this.createWorklogBean(entry,searchDate,defaultHoursPerDay);
                                }
                                worklogList
                                .append("<tr style=\"clear: both !important; display: block !important; Margin: 0 auto !important; max-width: 600px !important\">")
                                .append("<td style=\"padding-right:20px\">")
                                .append(dayMap.get(dayOfWeek).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                                .append("</td>")
                                .append("<td style=\"padding-right:20px\">")
                                .append("<b>")
                                .append(defaultHoursPerDay)
                                .append("</b>")
                                .append("</td>")
                                .append("<td>")
                                .append(defaultHoursPerDay.compareTo(BigDecimal.ZERO) > 0 ? iss.getActivity(entry.getDefaultActivity()).getDescription()
                                                                                          : "dna - already project/actions filled in")
                                .append("</td>")
                                .append("</tr>");}
                            );}
                    );
                    worklogList.append("</table>");
                    prefill.setPrefillWorklog(worklogList.toString());
                    mailService.sendPrefillTimeSheet(prefill);
                });
            }
        }
        private void createWorklogBean(UserBean userBean, Date searchDate, BigDecimal defaultHoursPerDay) {
            WorklogBean wlb = new WorklogBean();
            wlb.setId(null);
            wlb.setUserId(userBean.getId());
            wlb.setDay(searchDate);
            wlb.setLoggedMinutes(defaultHoursPerDay.longValue() * 60);
            wlb.setConfirmed(false);
            wlb.setActivity(iss.getActivity(userBean.getDefaultActivity()));
            log.info(wlb.toString());
            iof.createPrefillWorklog(wlb);
        }
}