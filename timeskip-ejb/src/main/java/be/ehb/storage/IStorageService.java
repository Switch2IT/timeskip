package be.ehb.storage;

import be.ehb.entities.config.ConfigBean;
import be.ehb.entities.identity.UserBean;
import be.ehb.entities.organizations.OrganizationBean;
import be.ehb.entities.organizations.OrganizationMembershipBean;
import be.ehb.entities.security.RoleBean;
import be.ehb.security.PermissionBean;

import java.util.Set;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public interface IStorageService {

    //Get

    OrganizationBean getOrganization(String organizationId);

    UserBean getUser(String userId);

    RoleBean getRole(String roleId);

    //Create

    UserBean createUser(UserBean user);

    //Update

    //Delete

    //Queries

    ConfigBean getDefaultConfig();

    Set<OrganizationMembershipBean> getMemberships(String userId);

    Set<PermissionBean> getPermissions(String userId);
}