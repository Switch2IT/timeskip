package be.ehb.rest.resources;

import be.ehb.facades.ISystemFacade;
import be.ehb.factories.ExceptionFactory;
import be.ehb.factories.ResponseFactory;
import be.ehb.i18n.Messages;
import be.ehb.model.requests.RestoreBackupRequest;
import be.ehb.model.responses.BackUpResponse;
import be.ehb.model.responses.ErrorResponse;
import be.ehb.model.responses.SystemStatusResponse;
import be.ehb.security.ISecurityContext;
import com.google.common.base.Preconditions;
import io.swagger.annotations.*;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.Response.Status.OK;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
@Api(value = "/system", description = "System-related endpoints")
@Path("/system")
@ApplicationScoped
public class SystemResource {

    @Inject
    private ISystemFacade systemFacade;
    @Inject
    private ISecurityContext securityContext;

    @ApiOperation(value = "Get system status",
            notes = "Get the system status.")
    @ApiResponses({
            @ApiResponse(code = 200, response = SystemStatusResponse.class, message = "System status"),
            @ApiResponse(code = 400, response = ErrorResponse.class, message = "Error occurred")
    })
    @GET
    @Path("/status")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSystemStatus() {
        return ResponseFactory.buildResponse(OK, systemFacade.getStatus());
    }

    @ApiOperation(value = "Backup data", notes = "Backup the database data to a JSON String.")
    @ApiResponses({
            @ApiResponse(code = 200, response = BackUpResponse.class, message = "Backup"),
            @ApiResponse(code = 400, response = ErrorResponse.class, message = "Error occurred")
    })
    @GET
    @Path("/backup")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBackup() {
        if (!securityContext.isAdmin()) throw ExceptionFactory.unauthorizedException();
        return ResponseFactory.buildResponse(OK, systemFacade.getBackup());
    }

    @ApiOperation(value = "Restore data", notes = "Restore the database data from a JSON String. Warning: this will delete the entire contents of the database and replace it with the contents of the request.")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Restored"),
            @ApiResponse(code = 400, response = ErrorResponse.class, message = "Error occurred")
    })
    @POST
    @Path("/backup/restore")
    @Produces(MediaType.APPLICATION_JSON)
    public Response restoreBackup(@ApiParam RestoreBackupRequest request) {
        if (!securityContext.isAdmin()) throw ExceptionFactory.unauthorizedException();
        Preconditions.checkNotNull(request, Messages.i18n.format("emptyRequestBody"));
        systemFacade.restoreBackup(request);
        return ResponseFactory.buildResponse(OK);
    }
}