package be.ehb.model.activities;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public class ActivityDTO {

    private String id;
    private String description;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ActivityDTO)) return false;

        ActivityDTO that = (ActivityDTO) o;

        return id != null ? id.equals(that.id) : that.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "ActivityDTO{" +
                "id='" + id + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}