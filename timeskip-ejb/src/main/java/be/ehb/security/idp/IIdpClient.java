package be.ehb.security.idp;

import be.ehb.entities.users.UserBean;

import java.security.Key;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public interface IIdpClient {
    Key getPublicKey(String realm, String keystoreId);

    UserBean createUser(UserBean user);

    void deleteUser(UserBean user);

    UserBean updateUser(UserBean user);
}
