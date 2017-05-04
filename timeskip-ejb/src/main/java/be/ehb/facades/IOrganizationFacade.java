package be.ehb.facades;

import be.ehb.model.organizations.OrganizationDTO;

import java.util.List;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public interface IOrganizationFacade {

    /**
     * Retrieve a list of all organizations
     *
     * @return List of organizations
     */
    List<OrganizationDTO> listOrganizations();

    /**
     * Get an organization by id
     *
     * @param organizationId the organization ID
     * @return Organization
     */
    OrganizationDTO getOrganization(String organizationId);

    /**
     * Get or create an organization
     *
     * @param organization the organization
     * @return Organization
     */
    OrganizationDTO createOrganization(OrganizationDTO organization);

    /**
     * Update an organization
     *
     * @param organization the organization
     * @return Organization
     */
    OrganizationDTO updateOrganization(OrganizationDTO organization);

    /**
     * Delete an organization
     *
     * @param organizationId the organization id
     */
    void deleteOrganization(String organizationId);

}
