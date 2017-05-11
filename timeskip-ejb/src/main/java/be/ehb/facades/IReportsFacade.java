package be.ehb.facades;

import be.ehb.model.responses.BillingReportResponse;
import be.ehb.model.responses.LoggedTimeReportResponse;
import be.ehb.model.responses.OverUnderTimeReportResponse;
import be.ehb.model.responses.UserLoggedTimeReportResponse;

import java.io.InputStream;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public interface IReportsFacade {

    // JSON reports

    OverUnderTimeReportResponse getOvertimeReport(String organizationId, String from, String to);

    OverUnderTimeReportResponse getUndertimeReport(String organizationId, String from, String to);

    LoggedTimeReportResponse getLoggedTimeReport(String organizationId, Long projectId, Long activityId, String from, String to);

    UserLoggedTimeReportResponse getCurrentUserReport(String organizationId, Long projectId, Long activityId, String from, String to);

    UserLoggedTimeReportResponse getUserReport(String organizationId, Long projectId, Long activityId, String userId, String from, String to);

    BillingReportResponse getBillingReport(String organizationId, Long projectId, Long activityId, String userId, String from, String to);

    // PDF Reports;

    InputStream getPdfOvertimeReport(String organizationId, String from, String to);

    InputStream getPdfUndertimeReport(String organizationId, String from, String to);

    InputStream getPdfLoggedTimeReport(String organizationId, Long projectId, Long activityId, String from, String to);

    InputStream getPdfCurrentUserReport(String organizationId, Long projectId, Long activityId, String from, String to);

    InputStream getPdfUserReport(String organizationId, Long projectId, Long activityId, String userId, String from, String to);

    InputStream getPdfBillingReport(String organizationId, Long projectId, Long activityId, String userId, String from, String to);
}
