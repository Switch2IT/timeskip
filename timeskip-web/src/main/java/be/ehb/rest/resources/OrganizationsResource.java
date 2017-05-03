package be.ehb.rest.resources;

import be.ehb.facades.IOrganizationFacade;
import be.ehb.factories.ExceptionFactory;
import be.ehb.model.organizations.OrganizationDTO;
import be.ehb.model.responses.ErrorResponse;
import be.ehb.security.ISecurityContext;
import be.ehb.security.PermissionType;
import com.google.common.base.Preconditions;
import io.swagger.annotations.*;
import io.swagger.jaxrs.PATCH;
import org.apache.commons.lang3.StringUtils;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
@Api(value = "/organizations", description = "Organizations-related endpoints")
@Path("/organizations")
@ApplicationScoped
public class OrganizationsResource {

    @Inject
    private IOrganizationFacade orgFacade;
    @Inject
    private ISecurityContext securityContext;

    @ApiOperation(value = "List organizations",
            notes = "Return a list of organizations")
    @ApiResponses({
            @ApiResponse(code = 200, responseContainer = "List", response = OrganizationDTO.class, message = "Organization list"),
            @ApiResponse(code = 400, response = ErrorResponse.class, message = "Error occurred")
    })
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<OrganizationDTO> listOrganizations() {
        return orgFacade.listOrganizations();
    }

    @ApiOperation(value = "Get organization",
            notes = "Get an organization for a given ID")
    @ApiResponses({
            @ApiResponse(code = 200, response = OrganizationDTO.class, message = "Organization"),
            @ApiResponse(code = 400, response = ErrorResponse.class, message = "Error occurred")
    })
    @GET
    @Path("/{organizationId}")
    @Produces(MediaType.APPLICATION_JSON)
    public OrganizationDTO getOrganization(@PathParam("organizationId") String organizationId) {
        Preconditions.checkArgument(StringUtils.isNotEmpty(organizationId), "Organization ID must be provided");
        if (!securityContext.hasPermission(PermissionType.ORG_VIEW, organizationId)) {
            throw ExceptionFactory.unauthorizedException(organizationId);
        }
        return orgFacade.getOrganization(organizationId);
    }

    @ApiOperation(value = "Create organization",
            notes = "Create an organization. A name must be provided")
    @ApiResponses({
            @ApiResponse(code = 200, response = OrganizationDTO.class, message = "Organization"),
            @ApiResponse(code = 400, response = ErrorResponse.class, message = "Error occurred")
    })
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public OrganizationDTO createOrganization(@ApiParam OrganizationDTO organization) {
        Preconditions.checkNotNull(organization, "Organization request must be provided");
        Preconditions.checkArgument(StringUtils.isNotEmpty(organization.getName()), "Organization name must be provided");
        if (!securityContext.isAdmin()) {
            throw ExceptionFactory.unauthorizedException();
        }
        return orgFacade.createOrganization(organization);
    }

    @ApiOperation(value = "Update organization",
            notes = "Update an organization. ID must be provided in the request body")
    @ApiResponses({
            @ApiResponse(code = 200, response = OrganizationDTO.class, message = "Updated"),
            @ApiResponse(code = 400, response = ErrorResponse.class, message = "Error occurred")
    })
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public OrganizationDTO updateOrganization(@ApiParam OrganizationDTO organization) {
        Preconditions.checkNotNull(organization, "Organization request must be provided");
        Preconditions.checkArgument(StringUtils.isNotEmpty(organization.getId()), "Organization ID must be provided");
        if (!securityContext.hasPermission(PermissionType.ORG_EDIT, organization.getId())) {
            throw ExceptionFactory.unauthorizedException(organization.getId());
        }
        Preconditions.checkArgument(StringUtils.isNotEmpty(organization.getName()), "Organization name must be provided");
        return orgFacade.updateOrganization(organization);
    }

    @ApiOperation(value = "Patch organization",
            notes = "Patch an organization.")
    @ApiResponses({
            @ApiResponse(code = 200, response = OrganizationDTO.class, message = "Updated"),
            @ApiResponse(code = 400, response = ErrorResponse.class, message = "Error occurred")
    })
    @PATCH
    @Path("/{organizationId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public OrganizationDTO patchOrganization(@PathParam("organizationId") String organizationId, @ApiParam OrganizationDTO organization) {
        Preconditions.checkArgument(StringUtils.isNotEmpty(organizationId), "Organization ID must be provided");
        if (!securityContext.hasPermission(PermissionType.ORG_EDIT, organizationId)) {
            throw ExceptionFactory.unauthorizedException(organizationId);
        }
        organization.setId(organizationId);
        Preconditions.checkNotNull(organization, "Organization request must be provided");
        return orgFacade.updateOrganization(organization);
    }

    @ApiOperation(value = "Delete organization",
            notes = "Delete an organization.")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Succesful, no content"),
            @ApiResponse(code = 400, response = ErrorResponse.class, message = "Error occurred")
    })
    @DELETE
    @Path("/{organizationId}")
    public void deleteOrganization(@PathParam("organizationId") String organizationId) {
        Preconditions.checkArgument(StringUtils.isNotEmpty(organizationId), "Organization ID must be provided");
        if (!securityContext.isAdmin()) {
            throw ExceptionFactory.unauthorizedException(organizationId);
        }
        orgFacade.deleteOrganization(organizationId);
    }

}