package be.ehb.facades;

import be.ehb.entities.security.RoleBean;
import be.ehb.factories.ExceptionFactory;
import be.ehb.factories.ResponseFactory;
import be.ehb.model.requests.NewRoleRequest;
import be.ehb.model.requests.UpdateRoleRequest;
import be.ehb.model.responses.RoleResponse;
import be.ehb.storage.IStorageService;
import be.ehb.utils.ConventionUtil;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@Default
public class RoleFacade implements IRoleFacade {

    private static final Logger log = LoggerFactory.getLogger(RoleFacade.class);

    @Inject
    private IStorageService storage;

    @Override
    public RoleResponse get(String roleId) {
        return ResponseFactory.createRoleResponse(storage.getRole(roleId));
    }

    @Override
    public List<RoleResponse> listRoles() {
        return storage.listRoles().parallelStream().map(ResponseFactory::createRoleResponse).collect(Collectors.toList());
    }

    @Override
    public RoleResponse create(NewRoleRequest request) {
        String idFromName = ConventionUtil.idFromName(request.getName());
        if (storage.findRoleByName(request.getName()) != null || storage.getRole(idFromName) != null) {
            throw ExceptionFactory.roleAlreadyExistsException(idFromName);
        }
        if (request.getAutoGrant() != null && request.getAutoGrant()) {
            RoleBean autoGrant = storage.getAutoGrantRole();
            autoGrant.setAutoGrant(false);
            storage.updateRole(autoGrant);
        }
        RoleBean role = new RoleBean();
        role.setId(idFromName);
        role.setName(request.getName());
        role.setDescription(request.getDescription());
        role.setAutoGrant(request.getAutoGrant());
        role.setPermissions(new ArrayList<>(request.getPermissions()));
        return ResponseFactory.createRoleResponse(storage.createRole(role));
    }

    @Override
    public RoleResponse update(String roleId, UpdateRoleRequest request) {
        RoleBean roleToUpdate = storage.getRole(roleId);
        if (storage.findRoleByName(request.getName()) != null) {
            throw ExceptionFactory.roleAlreadyExistsException(request.getName());
        }
        if (request.getAutoGrant() != null && request.getAutoGrant()) {
            RoleBean autoGrant = storage.getAutoGrantRole();
            autoGrant.setAutoGrant(false);
            storage.updateRole(autoGrant);
        }
        roleToUpdate.setName(request.getName());
        roleToUpdate.setDescription(request.getDescription());
        roleToUpdate.setAutoGrant(request.getAutoGrant());
        roleToUpdate.setPermissions(new ArrayList<>(request.getPermissions()));
        return ResponseFactory.createRoleResponse(storage.updateRole(roleToUpdate));
    }

    @Override
    public void delete(String roleId) {
        RoleBean roleToDelete = storage.getRole(roleId);
        if (CollectionUtils.isNotEmpty(storage.findMembershipsByRole(roleId))) {
            throw ExceptionFactory.roleStillInUseException(roleId);
        }
        storage.deleteRole(roleToDelete);
    }
}