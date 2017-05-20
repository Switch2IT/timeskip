package be.ehb.rest.resources;

import be.ehb.facades.IReportsFacade;
import be.ehb.factories.ExceptionFactory;
import be.ehb.factories.ResponseFactory;
import be.ehb.i18n.Messages;
import be.ehb.model.responses.*;
import be.ehb.security.ISecurityContext;
import be.ehb.security.PermissionType;
import com.google.common.base.Preconditions;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDate;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.InputStream;

import static javax.ws.rs.core.Response.Status.NO_CONTENT;
import static javax.ws.rs.core.Response.Status.OK;

/**
 * @author Guillaume Vandecasteele / Patrick Van den Bussche
 * @since 2017
 */
@Api(value = "/reports", description = "reports-related endpoints")
@Path("/reports")
@ApplicationScoped
public class ReportsResource {

    @Inject
    private IReportsFacade reportsFacade;
    @Inject
    private ISecurityContext securityContext;

    @ApiOperation(value = "Get Overtime Report", notes = "Get a report detailing which users have logged overtime. Dates must have a \"yyyy-MM-dd\" format.")
    @ApiResponses({
            @ApiResponse(code = 200, response = OverUnderTimeReportResponse.class, message = "Overtime report"),
            @ApiResponse(code = 400, response = ErrorResponse.class, message = "Error occurred")
    })
    @GET
    @Path("/overtime")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOvertimeReport(@QueryParam("organization") String organizationId,
                                      @QueryParam("from") String from,
                                      @QueryParam("to") String to) {
        checkOverUndertime(organizationId, from, to);
        return ResponseFactory.buildResponse(OK, reportsFacade.getOvertimeReport(organizationId, from, to));
    }

    @ApiOperation(value = "Get Undertime Report", notes = "Get a report detailing which users have fewer hours than required. Dates must have a \"yyyy-MM-dd\" format.")
    @ApiResponses({
            @ApiResponse(code = 200, response = OverUnderTimeReportResponse.class, message = "Undertime report"),
            @ApiResponse(code = 400, response = ErrorResponse.class, message = "Error occurred")
    })
    @GET
    @Path("/undertime")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUndertimeReport(@QueryParam("organization") String organizationId,
                                       @QueryParam("from") String from,
                                       @QueryParam("to") String to) {
        checkOverUndertime(organizationId, from, to);
        return ResponseFactory.buildResponse(OK, reportsFacade.getUndertimeReport(organizationId, from, to));
    }

    @ApiOperation(value = "Get Logged Time Report", notes = "Get a report detailing the total time that was logged per organization, project or activity for a given period. Dates must have a \"yyyy-MM-dd\" format.")
    @ApiResponses({
            @ApiResponse(code = 200, response = LoggedTimeReportResponse.class, message = "Logged time report"),
            @ApiResponse(code = 400, response = ErrorResponse.class, message = "Error occurred")
    })
    @GET
    @Path("/loggedtime")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLoggedTimeReport(@QueryParam("organization") String organizationId,
                                        @QueryParam("project") Long projectId,
                                        @QueryParam("activity") Long activityId,
                                        @QueryParam("from") String from,
                                        @QueryParam("to") String to) {
        checkDates(from, to);
        if (StringUtils.isNotEmpty(organizationId) && !securityContext.hasPermission(PermissionType.ORG_EDIT, organizationId))
            throw ExceptionFactory.unauthorizedException();
        return ResponseFactory.buildResponse(OK, reportsFacade.getLoggedTimeReport(organizationId, projectId, activityId, from, to));
    }

    @ApiOperation(value = "Get Current User Logged Time Report", notes = "Get a report detailing the total time that was logged per organization, project or activity for the current user during a given period. Dates must have a \"yyyy-MM-dd\" format.")
    @ApiResponses({
            @ApiResponse(code = 200, response = UserLoggedTimeReportResponse.class, message = "Logged time report"),
            @ApiResponse(code = 400, response = ErrorResponse.class, message = "Error occurred")
    })
    @GET
    @Path("/loggedtime/users/current")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCurrentUserReport(@QueryParam("organization") String organizationId,
                                         @QueryParam("project") Long projectId,
                                         @QueryParam("activity") Long activityId,
                                         @QueryParam("from") String from,
                                         @QueryParam("to") String to) {
        checkDates(from, to);
        return ResponseFactory.buildResponse(OK, reportsFacade.getCurrentUserReport(organizationId, projectId, activityId, from, to));
    }

    @ApiOperation(value = "Get User Logged Time Report", notes = "Get a report detailing the total time that was logged per organization, project or activity for a given user during a given period. Dates must have a \"yyyy-MM-dd\" format.")
    @ApiResponses({
            @ApiResponse(code = 200, response = UserLoggedTimeReportResponse.class, message = "Logged time report"),
            @ApiResponse(code = 400, response = ErrorResponse.class, message = "Error occurred")
    })
    @GET
    @Path("/loggedtime/users/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserReport(@PathParam("userId") String userId,
                                  @QueryParam("organization") String organizationId,
                                  @QueryParam("project") Long projectId,
                                  @QueryParam("activity") Long activityId,
                                  @QueryParam("from") String from,
                                  @QueryParam("to") String to) {
        Preconditions.checkArgument(StringUtils.isNotEmpty(userId), Messages.i18n.format("emptyPathParam", "User ID"));
        if (StringUtils.isNotEmpty(organizationId) && !securityContext.hasPermission(PermissionType.ORG_EDIT, organizationId))
            throw ExceptionFactory.unauthorizedException();
        checkDates(from, to);
        return ResponseFactory.buildResponse(OK, reportsFacade.getUserReport(organizationId, projectId, activityId, userId, from, to));
    }


    @ApiOperation(value = "Get Billing Report", notes = "Get a report detailing the total billable hours and amount due per organization, project, activity or user during a given period. Dates must have a \"yyyy-MM-dd\" format.")
    @ApiResponses({
            @ApiResponse(code = 200, response = BillingReportResponse.class, message = "Billing report"),
            @ApiResponse(code = 400, response = ErrorResponse.class, message = "Error occurred")
    })
    @GET
    @Path("/billing")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBillingReport(@QueryParam("organization") String organizationId,
                                     @QueryParam("project") Long projectId,
                                     @QueryParam("activity") Long activityId,
                                     @QueryParam("user") String userId,
                                     @QueryParam("from") String from,
                                     @QueryParam("to") String to) {
        checkDates(from, to);
        if (StringUtils.isNotEmpty(organizationId) && !securityContext.hasPermission(PermissionType.ORG_ADMIN, organizationId))
            throw ExceptionFactory.unauthorizedException();
        return ResponseFactory.buildResponse(OK, reportsFacade.getBillingReport(organizationId, projectId, activityId, userId, from, to));
    }

    @ApiOperation(value = "Get Overtime PDF Report", notes = "Get a report detailing which users have logged overtime in PDF format. Dates must have a \"yyyy-MM-dd\" format.")
    @ApiResponses({
            @ApiResponse(code = 200, response = Response.class, message = "Overtime PDF report"),
            @ApiResponse(code = 204, message = "No content"),
            @ApiResponse(code = 400, response = ErrorResponse.class, message = "Error occurred")
    })
    @GET
    @Path("overtime/pdf")
    @Produces("application/pdf")
    public Response getPdfOvertimeReport(@QueryParam("organization") String organizationId,
                                         @QueryParam("from") String from,
                                         @QueryParam("to") String to) {
        checkOverUndertime(organizationId, from, to);
        InputStream pdf = reportsFacade.getPdfOvertimeReport(organizationId, from, to);
        if (pdf != null)
            return ResponseFactory.buildResponse(OK, "Content-Disposition", "attachment; filename=OverTimeReport-" + new LocalDate().toString("yyyy-MM-dd") + ".pdf", pdf, null);
        else return ResponseFactory.buildResponse(NO_CONTENT);
    }

    @ApiOperation(value = "Get Undertime PDF Report", notes = "Get a report detailing which users have fewer hours than required in PDF format. Dates must have a \"yyyy-MM-dd\" format.")
    @ApiResponses({
            @ApiResponse(code = 200, response = Response.class, message = "Undertime PDF report"),
            @ApiResponse(code = 204, message = "No content"),
            @ApiResponse(code = 400, response = ErrorResponse.class, message = "Error occurred")
    })
    @GET
    @Path("undertime/pdf")
    @Produces("application/pdf")
    public Response getPdfUndertimeReport(@QueryParam("organization") String organizationId,
                                          @QueryParam("from") String from,
                                          @QueryParam("to") String to) {
        checkOverUndertime(organizationId, from, to);
        InputStream pdf = reportsFacade.getPdfUndertimeReport(organizationId, from, to);
        if (pdf != null)
            return ResponseFactory.buildResponse(OK, "Content-Disposition", "attachment; filename=UnderTimeReport-" + new LocalDate().toString("yyyy-MM-dd") + ".pdf", pdf, null);
        else return ResponseFactory.buildResponse(NO_CONTENT);
    }


    @ApiOperation(value = "Get Logged Time PDF Report", notes = "Get a report in PDF format detailing the total time that was logged per organization, project or activity for a given period. Dates must have a \"yyyy-MM-dd\" format.")
    @ApiResponses({
            @ApiResponse(code = 200, response = Response.class, message = "Logged time PDF report"),
            @ApiResponse(code = 204, message = "No content"),
            @ApiResponse(code = 400, response = ErrorResponse.class, message = "Error occurred")
    })
    @GET
    @Path("/loggedtime/pdf")
    @Produces("application/pdf")
    public Response getPdfLoggedTimeReport(@QueryParam("organization") String organizationId,
                                           @QueryParam("project") Long projectId,
                                           @QueryParam("activity") Long activityId,
                                           @QueryParam("from") String from,
                                           @QueryParam("to") String to) {
        checkDates(from, to);
        if (StringUtils.isNotEmpty(organizationId) && !securityContext.hasPermission(PermissionType.ORG_EDIT, organizationId))
            throw ExceptionFactory.unauthorizedException();
        InputStream pdf = reportsFacade.getPdfLoggedTimeReport(organizationId, projectId, activityId, from, to);
        if (pdf != null)
            return ResponseFactory.buildResponse(OK, "Content-Disposition", "attachment; filename=LoggedTimeReport-" + new LocalDate().toString("yyyy-MM-dd") + ".pdf", pdf, null);
        else return ResponseFactory.buildResponse(NO_CONTENT);
    }

    @ApiOperation(value = "Get Current User Logged Time PDF Report", notes = "Get a report in PDF format detailing the total time that was logged per organization, project or activity for the current user during a given period. Dates must have a \"yyyy-MM-dd\" format.")
    @ApiResponses({
            @ApiResponse(code = 200, response = Response.class, message = "Logged time PDF report"),
            @ApiResponse(code = 204, message = "No content"),
            @ApiResponse(code = 400, response = ErrorResponse.class, message = "Error occurred")
    })
    @GET
    @Path("/loggedtime/users/current/pdf")
    @Produces("application/pdf")
    public Response getPdfCurrentUserReport(@QueryParam("organization") String organizationId,
                                            @QueryParam("project") Long projectId,
                                            @QueryParam("activity") Long activityId,
                                            @QueryParam("from") String from,
                                            @QueryParam("to") String to) {
        checkDates(from, to);
        InputStream pdf = reportsFacade.getPdfCurrentUserReport(organizationId, projectId, activityId, from, to);
        if (pdf != null)
            return ResponseFactory.buildResponse(OK, "Content-Disposition", "attachment; filename=CurrentUserReport-" + new LocalDate().toString("yyyy-MM-dd") + ".pdf", pdf, null);
        else return ResponseFactory.buildResponse(NO_CONTENT);
    }

    @ApiOperation(value = "Get User Logged Time PDF Report", notes = "Get a report in PDF format detailing the total time that was logged per organization, project or activity for a given user during a given period. Dates must have a \"yyyy-MM-dd\" format.")
    @ApiResponses({
            @ApiResponse(code = 200, response = Response.class, message = "Logged time PDF report"),
            @ApiResponse(code = 204, message = "No content"),
            @ApiResponse(code = 400, response = ErrorResponse.class, message = "Error occurred")
    })
    @GET
    @Path("/loggedtime/users/{userId}/pdf")
    @Produces("application/pdf")
    public Response getPdfUserReport(@PathParam("userId") String userId,
                                     @QueryParam("organization") String organizationId,
                                     @QueryParam("project") Long projectId,
                                     @QueryParam("activity") Long activityId,
                                     @QueryParam("from") String from,
                                     @QueryParam("to") String to) {
        Preconditions.checkArgument(StringUtils.isNotEmpty(userId), Messages.i18n.format("emptyPathParam", "User ID"));
        if (StringUtils.isNotEmpty(organizationId) && !securityContext.hasPermission(PermissionType.ORG_EDIT, organizationId))
            throw ExceptionFactory.unauthorizedException();
        checkDates(from, to);
        InputStream pdf = reportsFacade.getPdfUserReport(organizationId, projectId, activityId, userId, from, to);
        if (pdf != null)
            return ResponseFactory.buildResponse(OK, "Content-Disposition", "attachment; filename=UserReport-" + new LocalDate().toString("yyyy-MM-dd") + ".pdf", pdf, null);
        else return ResponseFactory.buildResponse(NO_CONTENT);
    }

    @ApiOperation(value = "Get Billing PDF Report", notes = "Get a report in PDF format detailing the total billable hours and amount due per organization, project, activity or user during a given period. Dates must have a \"yyyy-MM-dd\" format.")
    @ApiResponses({
            @ApiResponse(code = 200, response = BillingReportResponse.class, message = "Billing PDF report"),
            @ApiResponse(code = 204, message = "No content"),
            @ApiResponse(code = 400, response = ErrorResponse.class, message = "Error occurred")
    })
    @GET
    @Path("billing/pdf")
    @Produces("application/pdf")
    public Response getPdfBillingReport(@QueryParam("organization") String organizationId,
                                        @QueryParam("project") Long projectId,
                                        @QueryParam("activity") Long activityId,
                                        @QueryParam("user") String userId,
                                        @QueryParam("from") String from,
                                        @QueryParam("to") String to) {
        checkDates(from, to);
        if (StringUtils.isNotEmpty(organizationId) && !securityContext.hasPermission(PermissionType.ORG_ADMIN, organizationId))
            throw ExceptionFactory.unauthorizedException();
        InputStream pdf = reportsFacade.getPdfBillingReport(organizationId, projectId, activityId, userId, from, to);
        if (pdf != null)
            return ResponseFactory.buildResponse(OK, "Content-Disposition", "attachment; filename=BillingReport-" + new LocalDate().toString("yyyy-MM-dd") + ".pdf", pdf, null);
        else return ResponseFactory.buildResponse(NO_CONTENT);
    }

    private void checkOverUndertime(String organizationId, String from, String to) {
        Preconditions.checkArgument(StringUtils.isNotEmpty(organizationId), Messages.i18n.format("missingQueryString", "organization"));
        if (!securityContext.hasPermission(PermissionType.ORG_EDIT, organizationId))
            throw ExceptionFactory.unauthorizedException(organizationId);
        checkDates(from, to);
    }

    private void checkDates(String from, String to) {
        Preconditions.checkArgument(StringUtils.isNotEmpty(from), Messages.i18n.format("missingQueryString", "from"));
        Preconditions.checkArgument(StringUtils.isNotEmpty(to), Messages.i18n.format("missingQueryString", "to"));
    }
}