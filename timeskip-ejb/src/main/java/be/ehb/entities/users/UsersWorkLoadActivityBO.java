package be.ehb.entities.users;

import java.util.Date;

/**
 * Created by Patrick Van den Busssche on 7/05/2017.
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
}
