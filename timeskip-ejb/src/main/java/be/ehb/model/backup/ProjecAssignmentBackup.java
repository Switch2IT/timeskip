package be.ehb.model.backup;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProjecAssignmentBackup implements Serializable {

    private Long projectId;
    private String userId;

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProjecAssignmentBackup)) return false;

        ProjecAssignmentBackup that = (ProjecAssignmentBackup) o;

        return projectId.equals(that.projectId) && userId.equals(that.userId);
    }

    @Override
    public int hashCode() {
        int result = projectId.hashCode();
        result = 31 * result + userId.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "ProjecAssignmentBackup{" +
                "projectId=" + projectId +
                ", userId='" + userId + '\'' +
                '}';
    }
}