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

    private void initConfig(ConfigBean optionalConfig) {
        ConfigBean defaultConfig;
        if (optionalConfig == null) {
            defaultConfig = storage.getDefaultConfig();
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
                log.info("IDP Default new user password: {}", getDefaultNewUserPassword());
                log.info("Validate JWT: {}", getValidateJWT());
                log.info("Notification mail will be sent from: {}", getNotificationMailFrom());
                log.info("Startup notification mail will be sent to: {}", getNoticationStartupMailTo());
                log.info(getReminderStartupLog());
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
    public String getNoticationStartupMailTo() {
        return config.getString(IConfig.NOTIFICATION_STARTUP_MAIL_TO);
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
    public String getDefaultNewUserPassword() {
        return config.getString(IConfig.IDP_DEFAULT_NEW_USER_PASSWORD);
    }

    @Override
    public String getIdpClient() {
        return config.getString(IConfig.IDP_CLIENT);
    }

    @Override
    public boolean getValidateJWT() {
        return config.getBoolean(IConfig.SECURITY_JWT_VALIDATION);
    }

    @Override
    public Integer getDayOfMonthlyReminderEmail() {
        return storage.getDefaultConfig().getDayOfMonthlyReminderEmail();
    }

    @Override
    public boolean getLastDayOfMonth() {
        return storage.getDefaultConfig().getLastDayOfMonth();
    }

    private String getReminderStartupLog() {
        StringBuilder rval = new StringBuilder("Reminder email will be sent on: ");
        if (getLastDayOfMonth()) {
            rval.append("last day of the month");
        } else {
            Integer day = getDayOfMonthlyReminderEmail();
            rval.append(day);
            switch (day) {
                case 1:
                    rval.append("st");
                    break;
                case 2:
                    rval.append("nd");
                case 3:
                    rval.append("rd");
                default:
                    rval.append("th");
            }
            rval.append(" day of the month");
        }
        return rval.toString();
    }
}