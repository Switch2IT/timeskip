package be.ehb.entities.organizations;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
@Entity
@Table(name = "organizations", schema = "timeskip")
public class OrganizationBean implements Serializable {

    @Id
    @Column(name = "id", nullable = false)
    private String id;
    @Column(name = "name", nullable = false)
    private String name;
    @Lob
    @Column(name = "description")
    @Type(type = "org.hibernate.type.TextType")
    private String description;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrganizationBean)) return false;

        OrganizationBean that = (OrganizationBean) o;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "OrganizationsBean{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}