package be.ehb.facades;

import be.ehb.entities.organizations.MembershipBean;
import be.ehb.factories.ResponseFactory;
import be.ehb.model.responses.MembershipResponse;
import be.ehb.storage.IStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.enterprise.inject.Default;
import javax.inject.Inject;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@Default
public class MembershipFacade implements IMembershipFacade {

    private static final Logger log = LoggerFactory.getLogger(MembershipFacade.class);

    @Inject
    private IRoleFacade roleFacade;
    @Inject
    private IOrganizationFacade orgFacade;
    @Inject
    private IUserFacade userFacade;
    @Inject
    private IStorageService storage;

    @Override
    public MembershipResponse create(String userId, String organizationId, String roleId) {
        orgFacade.get(organizationId);
        userFacade.get(userId);
        roleFacade.get(roleId);
        MembershipBean newMembership = new MembershipBean();
        newMembership.setRoleId(roleId);
        newMembership.setUserId(userId);
        newMembership.setRoleId(roleId);
        return ResponseFactory.createMembershipResponse(storage.createMembership(newMembership));
    }
}