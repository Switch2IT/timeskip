package be.ehb.facades;

import be.ehb.model.organizations.OrganizationDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.enterprise.inject.Default;
import java.util.List;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@Default
public class OrganizationFacade implements IOrganizationFacade {

    private static final Logger log = LoggerFactory.getLogger(OrganizationFacade.class);

    @Override
    public List<OrganizationDTO> listOrganizations() {
        return null;
    }

    @Override
    public OrganizationDTO getOrganization(String organizationId) {
        return null;
    }

    @Override
    public OrganizationDTO createOrganization(OrganizationDTO organization) {
        return null;
    }

    @Override
    public OrganizationDTO updateOrganization(OrganizationDTO organization) {
        return null;
    }

    @Override
    public void deleteOrganization(String organizationId) {

    }
}