package be.ehb.security;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public class PermissionBean {

    private PermissionType name;
    private String organizationId;

    public PermissionType getName() {
        return name;
    }

    public void setName(PermissionType name) {
        this.name = name;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PermissionBean)) return false;

        PermissionBean that = (PermissionBean) o;

        if (name != that.name) return false;
        return organizationId != null ? organizationId.equals(that.organizationId) : that.organizationId == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (organizationId != null ? organizationId.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "PermissionBean{" +
                "name=" + name +
                ", organizationId='" + organizationId + '\'' +
                '}';
    }
}