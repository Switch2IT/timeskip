package be.ehb.configuration;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public interface IAppConfig {

    String getNotificationMailFrom();

    String getNoticationStartupMailTo();

    String getVersion();

    String getBuildDate();

    String getConfigurationFile();

    String getIdpServerUrl();

    String getIdpRealm();

    String getIdpAdminClientId();

    String getIdpAdminClientSecret();

    String getIdpKeystoreId();

    String getIdpClient();

    String getDefaultNewUserPassword();

    boolean getValidateJWT();

    Integer getDayOfMonthlyReminderEmail();

    boolean getLastDayOfMonth();

    String getCurrencySymbol();

    String getPdfLogoLocation();
}
