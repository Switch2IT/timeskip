package be.ehb.model.responses;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SystemComponentsStatus implements Serializable {

    private Boolean timeskipApi;
    private Boolean keycloakIdp;

    public Boolean getTimeskipApi() {
        return timeskipApi;
    }

    public void setTimeskipApi(Boolean timeskipApi) {
        this.timeskipApi = timeskipApi;
    }

    public Boolean getKeycloakIdp() {
        return keycloakIdp;
    }

    public void setKeycloakIdp(Boolean keycloakIdp) {
        this.keycloakIdp = keycloakIdp;
    }

    @Override
    public String toString() {
        return "SystemComponentsStatus{" +
                "timeskipApi=" + timeskipApi +
                ", keycloakIdp=" + keycloakIdp +
                '}';
    }
}