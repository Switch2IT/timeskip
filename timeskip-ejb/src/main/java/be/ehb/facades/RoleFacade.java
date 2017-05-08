package be.ehb.facades;

import be.ehb.factories.ResponseFactory;
import be.ehb.model.responses.RoleResponse;
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
public class RoleFacade implements IRoleFacade {

    private static final Logger log = LoggerFactory.getLogger(RoleFacade.class);

    @Inject
    private IStorageService storage;

    @Override
    public RoleResponse get(String roleId) {
        return ResponseFactory.createRoleResponse(storage.getRole(roleId));
    }
}