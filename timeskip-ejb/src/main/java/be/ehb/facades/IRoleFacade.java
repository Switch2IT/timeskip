package be.ehb.facades;

import be.ehb.model.responses.RoleResponse;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public interface IRoleFacade {
    RoleResponse get(String roleId);
}
