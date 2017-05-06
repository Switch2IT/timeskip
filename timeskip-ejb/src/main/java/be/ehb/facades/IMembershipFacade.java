package be.ehb.facades;

import be.ehb.model.responses.MembershipResponse;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public interface IMembershipFacade {
    MembershipResponse create(String userId, String organizationId, String roleId);
}
