package be.ehb.storage;

import be.ehb.entities.config.ConfigBean;
import be.ehb.entities.identity.UserBean;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public interface IStorageService {

    ConfigBean getDefaultConfig();

    UserBean getUser(String userId);
}
