package be.ehb.model.responses;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SystemStatusResponse implements Serializable {

    private String id;
    private String name;
    private String description;
    private String moreInfo;
    private String builtOn;
    private String version;
    private Boolean up;
    private SystemComponentsStatus componentsUp;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMoreInfo() {
        return moreInfo;
    }

    public void setMoreInfo(String moreInfo) {
        this.moreInfo = moreInfo;
    }

    public String getBuiltOn() {
        return builtOn;
    }

    public void setBuiltOn(String builtOn) {
        this.builtOn = builtOn;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Boolean getUp() {
        return up;
    }

    public void setUp(Boolean up) {
        this.up = up;
    }

    public SystemComponentsStatus getComponentsUp() {
        return componentsUp;
    }

    public void setComponentsUp(SystemComponentsStatus componentsUp) {
        this.componentsUp = componentsUp;
    }

    @Override
    public String toString() {
        return "SystemStatusResponse{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", moreInfo='" + moreInfo + '\'' +
                ", builtOn='" + builtOn + '\'' +
                ", version='" + version + '\'' +
                ", up=" + up +
                ", componentsUp=" + componentsUp +
                '}';
    }
}