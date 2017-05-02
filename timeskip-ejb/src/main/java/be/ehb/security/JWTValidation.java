package be.ehb.security;

import be.ehb.configuration.IAppConfig;
import be.ehb.utils.CustomCollectors;
import be.ehb.utils.KeyUtils;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.jwt.consumer.JwtContext;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;
import javax.inject.Inject;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
@ApplicationScoped
@Default
public class JWTValidation {

    private static final String MASTER_REALM = "master";

    @Inject
    private IAppConfig config;

    public JwtContext getUnvalidatedContext(String jwt) throws InvalidJwtException, MalformedClaimException {
        return new JwtConsumerBuilder()
                .setSkipAllValidators()
                .setDisableRequireSignature()
                .setSkipSignatureVerification()
                .build()
                .process(jwt);
    }

    public JwtContext getValidatedContext(String jwt) throws InvalidJwtException, MalformedClaimException {
        //Get the IDP public key

        return new JwtConsumerBuilder()
                .setSkipAllValidators()
                .setVerificationKey(KeyUtils.getPublicKey(createKeycloakClient().realm(config.getIdpRealm()).keys().getKeyMetadata().getKeys().stream()
                        .filter(key -> key.getKid().equals(config.getIdpKeystoreId()))
                        .collect(CustomCollectors.getSingleResult())
                        .getPublicKey()))
                .build()
                .process(jwt);
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