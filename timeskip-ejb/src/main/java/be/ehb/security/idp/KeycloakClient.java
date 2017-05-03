package be.ehb.security.idp;

import be.ehb.configuration.IAppConfig;
import be.ehb.utils.CustomCollectors;
import be.ehb.utils.KeyUtils;
import org.apache.commons.lang3.StringUtils;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Singleton;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import java.security.Key;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
@ApplicationScoped
@Singleton
@Default
public class KeycloakClient implements IIdpClient {

    private static final Logger log = LoggerFactory.getLogger(KeycloakClient.class);

    private static final String MASTER_REALM = "master";

    @Inject
    private IAppConfig config;

    @Override
    public Key getPublicKey(String realm, String keystoreId) {
        try {
            return KeyUtils.getPublicKey(createKeycloakClient().realm(realm).keys().getKeyMetadata().getKeys().stream()
                    .filter(key -> key != null && StringUtils.isNotEmpty(key.getKid()) && key.getKid().equals(keystoreId))
                    .collect(CustomCollectors.getSingleResult())
                    .getPublicKey());
        }
        catch (Exception ex) {
            ex.printStackTrace();
            log.error("Could not retrieve IDP public key {} for realm {}", keystoreId, realm);
            return null;
        }
    }

    private Keycloak createKeycloakClient() {
        return KeycloakBuilder.builder()
                .serverUrl(config.getIdpServerUrl())
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .realm(MASTER_REALM)
                .clientId(config.getIdpAdminClientId())
                .resteasyClient(new ResteasyClientBuilder().connectionPoolSize(10)
                        .build())
                .clientSecret(config.getIdpAdminClientSecret())
                .build();
    }
}