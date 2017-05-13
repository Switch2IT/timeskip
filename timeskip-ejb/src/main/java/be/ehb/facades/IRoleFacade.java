package be.ehb.facades;

import be.ehb.model.requests.NewRoleRequest;
import be.ehb.model.requests.UpdateRoleRequest;
import be.ehb.model.responses.RoleResponse;

import java.util.List;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public interface IRoleFacade {

    List<RoleResponse> listRoles();

    RoleResponse get(String roleId);

    RoleResponse create(NewRoleRequest request);

    RoleResponse update(String roleId, UpdateRoleRequest request);

    void delete(String roleId);
}
