package be.ehb.configuration;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public interface IAppConfig {
    String getNotificationMailFrom();

    String getVersion();

    String getBuildDate();

    String getConfigurationFile();

    String getIdpServerUrl();

    String getIdpRealm();

    String getIdpClientId();

    String getIdpClientSecret();

    String getIdpKeystoreId();
}
