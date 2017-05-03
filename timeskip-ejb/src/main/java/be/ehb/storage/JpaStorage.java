package be.ehb.storage;

import be.ehb.entities.config.ConfigBean;
import be.ehb.entities.identity.UserBean;
import be.ehb.entities.organizations.OrganizationBean;
import be.ehb.entities.organizations.OrganizationMembershipBean;
import be.ehb.entities.security.RoleBean;
import be.ehb.security.PermissionBean;
import be.ehb.security.PermissionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;
import javax.persistence.NoResultException;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
@ApplicationScoped
@Default
public class JpaStorage extends AbstractJpaStorage implements IStorageService {

    private static final Logger log = LoggerFactory.getLogger(JpaStorage.class);

    @Override
    public ConfigBean getDefaultConfig() {
        try {
            return (ConfigBean) getActiveEntityManager().createQuery("SELECT c FROM ConfigBean c WHERE c.defaultConfig = TRUE").getSingleResult();
        } catch (NoResultException ex) {
            log.error("No results found: {}", ex.getMessage());
            return null;
        }
    }

    @Override
    public UserBean getUser(String userId) {
        return super.get(userId, UserBean.class);
    }

    @Override
    public UserBean createUser(UserBean user) {
        return super.create(user);
    }

    @Override
    public RoleBean getRole(String roleId) {
        return super.get(roleId, RoleBean.class);
    }

    @Override
    public Set<OrganizationMembershipBean> getMemberships(String userId) {
        Set<OrganizationMembershipBean> rval = new HashSet<>();
        rval.addAll(getActiveEntityManager()
                .createQuery("SELECT m FROM OrganizationMembershipBean m WHERE m.userId = :userId")
                .setParameter("userId", userId)
                .getResultList());
        return rval;
    }

    @Override
    public Set<PermissionBean> getPermissions(String userId) {
        Set<PermissionBean> rval = new HashSet<>();
        Set<OrganizationMembershipBean> memberships = getMemberships(userId);
        for (OrganizationMembershipBean membership : memberships) {
            RoleBean role = getRole(membership.getRoleId());
            for (PermissionType permission : role.getPermissions()) {
                PermissionBean pb = new PermissionBean();
                pb.setName(permission);
                pb.setOrganizationId(membership.getOrganizationId());
                rval.add(pb);
            }
        }
        return rval;
    }

    @Override
    public OrganizationBean getOrganization(String organizationId) {
        return super.get(organizationId, OrganizationBean.class);
    }
}