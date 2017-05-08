package be.ehb.rest.resources;

import be.ehb.facades.ISystemFacade;
import be.ehb.factories.ExceptionFactory;
import be.ehb.factories.ResponseFactory;
import be.ehb.mail.MailTopic;
import be.ehb.model.requests.UpdateDayOfMonthlyReminderRequest;
import be.ehb.model.requests.UpdateMailTemplateRequest;
import be.ehb.model.responses.DayOfMonthlyReminderResponse;
import be.ehb.model.responses.ErrorResponse;
import be.ehb.model.responses.MailTemplateResponse;
import be.ehb.security.ISecurityContext;
import com.google.common.base.Preconditions;
import io.swagger.annotations.*;
import io.swagger.jaxrs.PATCH;

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
    private ISystemFacade systemFacade;
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
        return ResponseFactory.buildResponse(OK, systemFacade.listMailTemplates());
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
        Preconditions.checkNotNull(topic, "Topic must be provided");
        return ResponseFactory.buildResponse(OK, systemFacade.listMailTemplates());
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
        Preconditions.checkNotNull(topic, "Topic must be provided");
        Preconditions.checkNotNull(request, "Request body must be provided");
        if (!securityContext.isAdmin()) {
            throw ExceptionFactory.unauthorizedException("Admin privileges required");
        }
        return ResponseFactory.buildResponse(OK, systemFacade.updateMailTemplate(topic, request));
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
        return ResponseFactory.buildResponse(OK, systemFacade.getDayOfMonthlyReminder());
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
        Preconditions.checkNotNull(request, "Request body must be provided");
        Preconditions.checkArgument((request.getLastDayOfMonth() != null && request.getLastDayOfMonth()) || request.getDayOfMonthlyReminder() != null, "\"lastDayOfMonth\" should either be true, or \"dayOfMonthlyReminder\" should be provided");
        if (request.getLastDayOfMonth() == null || !request.getLastDayOfMonth()) {
            Preconditions.checkArgument(request.getDayOfMonthlyReminder() > 0 && request.getDayOfMonthlyReminder() < 29, "If \"lastDaysOfMonth\" isn't set to true, \"dayOfMonthlyReminder\" should be a value between 1 and 28");
        }
        if (!securityContext.isAdmin()) {
            throw ExceptionFactory.unauthorizedException("Admin privileges required");
        }
        return ResponseFactory.buildResponse(OK, systemFacade.updateDayOfMonthlyReminder(request));
    }
}