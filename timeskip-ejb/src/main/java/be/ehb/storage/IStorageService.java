package be.ehb.storage;

import be.ehb.entities.config.ConfigBean;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public interface IStorageService {

    ConfigBean getDefaultConfig();
}
