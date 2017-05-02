package be.ehb.facades;

import be.ehb.security.Permission;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.enterprise.inject.Default;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@Default
public class SecurityFacade implements ISecurityFacade {

    private static final Logger log = LoggerFactory.getLogger(SecurityFacade.class);

    @Override
    public boolean hasPermission(Permission permission, String entityId) {
        return false;
    }
}