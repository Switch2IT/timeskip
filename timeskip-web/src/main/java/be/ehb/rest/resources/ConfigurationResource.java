package be.ehb.rest.resources;

import be.ehb.facades.IManagementFacade;
import be.ehb.factories.ExceptionFactory;
import be.ehb.factories.ResponseFactory;
import be.ehb.i18n.Messages;
import be.ehb.mail.MailTopic;
import be.ehb.model.requests.NewPaygradeRequest;
import be.ehb.model.requests.UpdateDayOfMonthlyReminderRequest;
import be.ehb.model.requests.UpdateMailTemplateRequest;
import be.ehb.model.requests.UpdatePaygradeRequest;
import be.ehb.model.responses.DayOfMonthlyReminderResponse;
import be.ehb.model.responses.ErrorResponse;
import be.ehb.model.responses.MailTemplateResponse;
import be.ehb.model.responses.PaygradeResponse;
import be.ehb.security.ISecurityContext;
import com.google.common.base.Preconditions;
import io.swagger.annotations.*;
import io.swagger.jaxrs.PATCH;
import org.apache.commons.lang3.StringUtils;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.Response.Status.OK;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
@Api(value = "/configuration", description = "Configuration-related endpoints")
@Path("/configuration")
@ApplicationScoped
public class ConfigurationResource {

    @Inject
    private IManagementFacade managementFacade;
    @Inject
    private ISecurityContext securityContext;

    @ApiOperation(value = "List mail templates",
            notes = "List all available mail templates")
    @ApiResponses({
            @ApiResponse(code = 200, responseContainer = "List", response = MailTemplateResponse.class, message = "Mail template"),
            @ApiResponse(code = 400, response = ErrorResponse.class, message = "Error occurred")
    })
    @GET
    @Path("/mail/templates")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listMailTemplates() {
        return ResponseFactory.buildResponse(OK, managementFacade.listMailTemplates());
    }

    @ApiOperation(value = "List mail templates",
            notes = "List all available mail templates")
    @ApiResponses({
            @ApiResponse(code = 200, response = MailTemplateResponse.class, message = "Mail template"),
            @ApiResponse(code = 400, response = ErrorResponse.class, message = "Error occurred")
    })
    @GET
    @Path("/mail/templates/{topic}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMailTemplate(@PathParam("topic") MailTopic topic) {
        Preconditions.checkNotNull(topic, Messages.i18n.format("emptyPathParam", "Topic"));
        return ResponseFactory.buildResponse(OK, managementFacade.getMailTemplate(topic));
    }

    @ApiOperation(value = "List mail templates",
            notes = "List all available mail templates")
    @ApiResponses({
            @ApiResponse(code = 200, response = MailTemplateResponse.class, message = "Mail template"),
            @ApiResponse(code = 400, response = ErrorResponse.class, message = "Error occurred")
    })
    @PATCH
    @Path("/mail/templates/{topic}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateMailTemplate(@PathParam("topic") MailTopic topic, @ApiParam UpdateMailTemplateRequest request) {
        Preconditions.checkNotNull(topic, Messages.i18n.format("emptyPathParam", "Topic"));
        Preconditions.checkNotNull(request, Messages.i18n.format("emptyRequestBody"));
        if (!securityContext.isAdmin()) {
            throw ExceptionFactory.unauthorizedException("Mail Templates");
        }
        return ResponseFactory.buildResponse(OK, managementFacade.updateMailTemplate(topic, request));
    }

    @ApiOperation(value = "Update day of monthly reminder",
            notes = "Update the day on which the monthly reminder to confirm work logs should be sent. if \"lastDayOfMonth\" is set to true, the reminder will be send on the last day of every month. If it should be sent on another day, please provide a value between 1 & 28")
    @ApiResponses({
            @ApiResponse(code = 200, response = DayOfMonthlyReminderResponse.class, message = "Day of monthly reminder"),
            @ApiResponse(code = 400, response = ErrorResponse.class, message = "Error occurred")
    })
    @GET
    @Path("/schedule/dayofmonthlyreminder")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDayOfMonthlyReminder() {
        return ResponseFactory.buildResponse(OK, managementFacade.getDayOfMonthlyReminder());
    }

    @ApiOperation(value = "Update day of monthly reminder",
            notes = "Update the day on which the monthly reminder to confirm work logs should be sent. if \"lastDayOfMonth\" is set to true, the reminder will be send on the last day of every month. If it should be sent on another day, please provide a value between 1 & 28")
    @ApiResponses({
            @ApiResponse(code = 200, response = DayOfMonthlyReminderResponse.class, message = "Day of monthly reminder"),
            @ApiResponse(code = 400, response = ErrorResponse.class, message = "Error occurred")
    })
    @PUT
    @Path("/schedule/dayofmonthlyreminder")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateDayOfMonthlyReminder(@ApiParam UpdateDayOfMonthlyReminderRequest request) {
        Preconditions.checkNotNull(request, Messages.i18n.format("emptyRequestBody"));
        Preconditions.checkArgument((request.getLastDayOfMonth() != null && request.getLastDayOfMonth()) || request.getDayOfMonthlyReminder() != null, Messages.i18n.format("invalidLastDayOfMonth"));
        if (request.getLastDayOfMonth() == null || !request.getLastDayOfMonth()) {
            Preconditions.checkArgument(request.getDayOfMonthlyReminder() != null && request.getDayOfMonthlyReminder() > 0 && request.getDayOfMonthlyReminder() < 29, "invalidDayOfMonthlyReminder");
        }
        if (!securityContext.isAdmin()) {
            throw ExceptionFactory.unauthorizedException("Monthly Reminder");
        }
        return ResponseFactory.buildResponse(OK, managementFacade.updateDayOfMonthlyReminder(request));
    }

    @ApiOperation(value = "List paygrades",
            notes = "List all available paygrades")
    @ApiResponses({
            @ApiResponse(code = 200, responseContainer = "List", response = MailTemplateResponse.class, message = "Mail template"),
            @ApiResponse(code = 400, response = ErrorResponse.class, message = "Error occurred")
    })
    @GET
    @Path("/mail/paygrades")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listPaygrades() {
        return ResponseFactory.buildResponse(OK, managementFacade.listPaygrades());
    }

    @ApiOperation(value = "Get paygrade",
            notes = "Get a paygrade for a provided ID.")
    @ApiResponses({
            @ApiResponse(code = 200, response = MailTemplateResponse.class, message = "Mail template"),
            @ApiResponse(code = 400, response = ErrorResponse.class, message = "Error occurred")
    })
    @GET
    @Path("/paygrades/{paygradeId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPaygrade(@PathParam("paygradeId") Long paygradeId) {
        Preconditions.checkNotNull(paygradeId, Messages.i18n.format("emptyPathParam", "Paygrade ID"));
        return ResponseFactory.buildResponse(OK, managementFacade.getPaygrade(paygradeId));
    }

    @ApiOperation(value = "Create paygrade",
            notes = "Create a paygrade.")
    @ApiResponses({
            @ApiResponse(code = 201, response = PaygradeResponse.class, message = "Created"),
            @ApiResponse(code = 400, response = ErrorResponse.class, message = "Error occurred")
    })
    @POST
    @Path("/paygrades")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createPaygrade(@ApiParam NewPaygradeRequest request) {
        Preconditions.checkNotNull(request, Messages.i18n.format("emptyRequestBody"));
        Preconditions.checkArgument(StringUtils.isNotEmpty(request.getName()), Messages.i18n.format("emptyField", "name"));
        Preconditions.checkNotNull(request.getHourlyRate(), Messages.i18n.format("emptyField", "hourlyRate"));
        Preconditions.checkArgument(request.getHourlyRate() >= 0, Messages.i18n.format("greaterThanZero", "hourlyRate"));
        if (!securityContext.isAdmin()) {
            throw ExceptionFactory.unauthorizedException("Paygrades");
        }
        return ResponseFactory.buildResponse(OK, managementFacade.createPaygrade(request));
    }

    @ApiOperation(value = "Update paygrade",
            notes = "Update the paygrade with provided ID")
    @ApiResponses({
            @ApiResponse(code = 200, response = PaygradeResponse.class, message = "Updated"),
            @ApiResponse(code = 400, response = ErrorResponse.class, message = "Error occurred")
    })
    @PATCH
    @Path("/paygrades/{paygradeId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updatePaygrade(@PathParam("paygradeId") Long paygradeId, @ApiParam UpdatePaygradeRequest request) {
        Preconditions.checkNotNull(paygradeId, Messages.i18n.format("emptyPathParam", "Paygrade ID"));
        Preconditions.checkNotNull(request, Messages.i18n.format("emptyRequestBody"));
        Preconditions.checkNotNull(request.getHourlyRate(), Messages.i18n.format("emptyField", "hourlyRate"));
        Preconditions.checkArgument(request.getHourlyRate() >= 0, Messages.i18n.format("greaterThanZero", "hourlyRate"));
        if (!securityContext.isAdmin()) {
            throw ExceptionFactory.unauthorizedException("Paygrades");
        }
        return ResponseFactory.buildResponse(OK, managementFacade.updatePaygrade(paygradeId, request));
    }

    @ApiOperation(value = "Delete paygrade",
            notes = "Delete the paygrade with provided ID")
    @ApiResponses({
            @ApiResponse(code = 204, response = PaygradeResponse.class, message = "Deleted"),
            @ApiResponse(code = 400, response = ErrorResponse.class, message = "Error occurred")
    })
    @PATCH
    @Path("/paygrades/{paygradeId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deletePaygrade(@PathParam("paygradeId") Long paygradeId) {
        Preconditions.checkNotNull(paygradeId, Messages.i18n.format("emptyPathParam", "Paygrade ID"));
        if (!securityContext.isAdmin()) {
            throw ExceptionFactory.unauthorizedException("Paygrades");
        }
        managementFacade.deletePaygrade(paygradeId);
        return ResponseFactory.buildResponse(OK);
    }
}