package be.ehb.model.requests;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.List;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateCurrentUserWorklogRequestList implements Serializable {

    private List<UpdateCurrentUserWorklogRequest> updateCurrentUserWorklogRequests;

    public List<UpdateCurrentUserWorklogRequest> getUpdateCurrentUserWorklogRequests() {
        return updateCurrentUserWorklogRequests;
    }

    public void setUpdateCurrentUserWorklogRequests(List<UpdateCurrentUserWorklogRequest> updateCurrentUserWorklogRequests) {
        this.updateCurrentUserWorklogRequests = updateCurrentUserWorklogRequests;
    }

    @Override
    public String toString() {
        return "UpdateCurrentUserWorklogRequestList{" +
                "updateCurrentUserWorklogRequests=" + updateCurrentUserWorklogRequests +
                '}';
    }
}