package be.ehb.security;

import java.util.*;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public class IndexedPermissions {

    private Set<String> qualifiedPermissions = new HashSet<>();
    private Map<PermissionType, Set<String>> permissionToOrgsMap = new HashMap<>();

    public IndexedPermissions(Set<PermissionBean> permissions) {
        index(permissions);
    }

    public boolean hasQualifiedPermission(PermissionType permissionName, String orgQualifier) {
        String key = createQualifiedPermissionKey(permissionName, orgQualifier);
        return qualifiedPermissions.contains(key);
    }

    public Set<String> getOrgQualifiers(PermissionType permissionName) {
        Set<String> orgs = permissionToOrgsMap.get(permissionName);
        if (orgs == null)
            orgs = Collections.EMPTY_SET;
        return Collections.unmodifiableSet(orgs);
    }

    private void index(Set<PermissionBean> permissions) {
        for (PermissionBean permissionBean : permissions) {
            PermissionType permissionName = permissionBean.getName();
            String orgQualifier = permissionBean.getOrganizationId();
            String qualifiedPermission = createQualifiedPermissionKey(permissionName, orgQualifier);
            qualifiedPermissions.add(qualifiedPermission);
            Set<String> orgs = permissionToOrgsMap.get(permissionName);
            if (orgs == null) {
                orgs = new HashSet<>();
                permissionToOrgsMap.put(permissionName, orgs);
            }
            orgs.add(orgQualifier);
        }
    }

    protected String createQualifiedPermissionKey(PermissionType permissionName, String orgQualifier) {
        return permissionName.name() + "||" + orgQualifier;
    }

}