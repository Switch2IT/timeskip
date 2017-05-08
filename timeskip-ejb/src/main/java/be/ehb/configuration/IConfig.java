package be.ehb.configuration;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
interface IConfig {

    String PROP_FILE_DATE = "date";
    String PROP_FILE_VERSION = "version";
    String PROP_FILE_CONFIG_FILE = "configuration.file";

    String NOTIFICATION_MAIL_FROM = "timeskip.notifications.mail_from";
    String NOTIFICATION_STARTUP_MAIL_TO = "timeskip.notifications.startup_mail_to";

    String IDP_CLIENT_ID = "timeskip.idp.admin_client_id";
    String IDP_CLIENT_SECRET = "timeskip.idp.admin_client_secret";
    String IDP_REALM = "timeskip.idp.realm";
    String IDP_CLIENT = "timeskip.idp.client";
    String IDP_SERVER_URL = "timeskip.idp.server_url";
    String IDP_KEYSTORE_ID = "timeskip.idp.keystore_id";

    String SECURITY_JWT_VALIDATION = "timeskip.security.validate_jwt";
}
