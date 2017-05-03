package be.ehb.configuration;

import be.ehb.entities.config.ConfigBean;
import be.ehb.factories.ExceptionFactory;
import be.ehb.storage.IStorageService;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
@Singleton(name = "AppConfig")
@Startup
@Default
public class AppConfig implements Serializable, IAppConfig {

    private static final Logger log = LoggerFactory.getLogger(AppConfig.class);

    private static Config config;
    private static Properties properties;

    @Inject
    private IStorageService storage;

    @PostConstruct
    public void postInit() {
        initConfig(null);
    }

    public void initConfig(ConfigBean optionalConfig) {
        ConfigBean defaultConfig;
        if (optionalConfig == null) {
            defaultConfig = storage.getDefaultConfig();
            if (defaultConfig == null) throw ExceptionFactory.storageException("No configuration found.");
        } else {
            defaultConfig = optionalConfig;
        }
        Path configPath = Paths.get(defaultConfig.getConfigPath());
        log.info("Config path loaded:{}", configPath.toAbsolutePath());

        InputStream is = getClass().getClassLoader().getResourceAsStream("application.properties");
        properties = new Properties();
        if (is != null) {
            try {
                properties.load(is);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else throw ExceptionFactory.systemErrorException("Timeskip basic property file not found.");

        if (!configPath.toFile().exists()) {
            throw ExceptionFactory.systemErrorException("Config property file not found.");
        }
        config = ConfigFactory.parseFile(configPath.toFile());

        if (config == null) {
            throw ExceptionFactory.systemErrorException("Config property file not found.");
        } else {
            try {
                log.info("============================== Timeskip Configuration ==============================");
                log.info("Version: {}", getVersion());
                log.info("Build date: {}", getBuildDate());
                log.info("Configuration file: {}", getConfigurationFile());
                log.info("IDP server URL: {}", getIdpServerUrl());
                log.info("IDP realm: {}", getIdpRealm());
                log.info("IDP admin client: {}", getIdpAdminClientId());
                log.info("IDP admin secret: ***************{}", getIdpAdminClientSecret().substring(getIdpAdminClientSecret().length() - 5));
                log.info("IDP keystore ID: {}", getIdpKeystoreId());
                log.info("IDP Client: {}", getIdpClient());
                log.info("Validate JWT: {}", getValidateJWT());
                log.info("Notification mail will be send from: {}", getNotificationMailFrom());
                log.info("====================================================================================");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public String getNotificationMailFrom() {
        return config.getString(IConfig.NOTIFICATION_MAIL_FROM);
    }

    @Override
    public String getVersion() {
        return properties.getProperty(IConfig.PROP_FILE_VERSION);
    }

    @Override
    public String getBuildDate() {
        return properties.getProperty(IConfig.PROP_FILE_DATE);
    }

    @Override
    public String getConfigurationFile() {
        return properties.getProperty(IConfig.PROP_FILE_CONFIG_FILE);
    }

    @Override
    public String getIdpServerUrl() {
        return config.getString(IConfig.IDP_SERVER_URL);
    }

    @Override
    public String getIdpRealm() {
        return config.getString(IConfig.IDP_REALM);
    }

    @Override
    public String getIdpAdminClientId() {
        return config.getString(IConfig.IDP_CLIENT_ID);
    }

    @Override
    public String getIdpAdminClientSecret() {
        return config.getString(IConfig.IDP_CLIENT_SECRET);
    }

    @Override
    public String getIdpKeystoreId() {
        return config.getString(IConfig.IDP_KEYSTORE_ID);
    }

    @Override
    public String getIdpClient() {
        return config.getString(IConfig.IDP_CLIENT);
    }

    @Override
    public boolean getValidateJWT() {
        return config.getBoolean(IConfig.SECURITY_JWT_VALIDATION);
    }
}