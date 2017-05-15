package be.ehb.entities.users;

import java.util.Date;

/**
 * @author Patrick Van den Bussche
 * @since 2017
 */
public class UsersWorkLoadActivityBO {
    //Users
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    //WorkLoad
    private Date day;
    private Long loggedMinutes;
    private Boolean confirmed;
    //Activity
    private String description;

    public UsersWorkLoadActivityBO(String id, String firstName, String lastName, String email, Date day, Long loggedMinutes, Boolean confirmed, String description) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.day = day;
        this.loggedMinutes = loggedMinutes;
        this.confirmed = confirmed;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public Date getDay() {
        return day;
    }

    public Long getLoggedMinutes() {
        return loggedMinutes;
    }

    public Boolean getConfirmed() {
        return confirmed;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "UsersWorkLoadActivityBO{" +
                "id='" + id + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", day=" + day +
                ", loggedMinutes=" + loggedMinutes +
                ", confirmed=" + confirmed +
                ", description='" + description + '\'' +
                '}';
    }
}
