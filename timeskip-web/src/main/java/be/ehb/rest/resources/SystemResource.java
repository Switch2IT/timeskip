package be.ehb.rest.resources;

import be.ehb.facades.ISystemFacade;
import be.ehb.factories.ResponseFactory;
import be.ehb.model.responses.ErrorResponse;
import be.ehb.model.responses.SystemStatusResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
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

    @ApiOperation(value = "Get system status",
            notes = "Get the system status")
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

}