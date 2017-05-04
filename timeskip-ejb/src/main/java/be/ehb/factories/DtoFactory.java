package be.ehb.factories;

import be.ehb.entities.identity.UserBean;
import be.ehb.model.users.UserDTO;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public class DtoFactory {

    public static UserDTO createUserDTO(UserBean user) {
        UserDTO rval = null;
        if (user != null) {
            rval = new UserDTO();
            rval.setId(user.getId());
            rval.setFullName(user.getFullName());
            rval.setEmail(user.getEmail());
            rval.setAdmin(user.getAdmin());
        }
        return rval;
    }
}