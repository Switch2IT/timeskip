package be.ehb.security.idp;

import be.ehb.configuration.IAppConfig;
import be.ehb.entities.users.UserBean;
import be.ehb.factories.ExceptionFactory;
import be.ehb.utils.CustomCollectors;
import be.ehb.utils.KeyUtils;
import org.apache.commons.lang3.StringUtils;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Singleton;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import java.security.Key;
import java.util.List;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
@ApplicationScoped
@Singleton
@Default
public class KeycloakClient implements IIdpClient {

    private static final Logger log = LoggerFactory.getLogger(KeycloakClient.class);

    @Inject
    private IAppConfig config;

    @Override
    public Key getPublicKey(String realm, String keystoreId) {
        try {
            return KeyUtils.getPublicKey(createKeycloakClient().realm(realm).keys().getKeyMetadata().getKeys().stream()
                    .filter(key -> key != null && StringUtils.isNotEmpty(key.getKid()) && key.getKid().equals(keystoreId))
                    .collect(CustomCollectors.getSingleResult())
                    .getPublicKey());
        } catch (Exception ex) {
            log.error("Could not retrieve IDP public key {} for realm {}: {}", keystoreId, realm, ex);
            return null;
        }
    }

    @Override
    public UserBean createUser(UserBean user) {
        UsersResource users = createKeycloakClient().realm(config.getIdpRealm()).users();
        //Check if user already exists on the IDP
        List<UserRepresentation> reps = users.search(user.getEmail(), user.getFirstName(), user.getLastName(), user.getEmail(), null, null);
        if (reps == null || reps.isEmpty()) {
            UserRepresentation rep = new UserRepresentation();
            rep.setEmail(user.getEmail());
            rep.setEmailVerified(true);
            rep.setEnabled(true);
            rep.setUsername(rep.getEmail());
            users.create(rep);
            users.search(user.getEmail(), user.getFirstName(), user.getLastName(), user.getEmail(), null, null);
            if (reps == null || reps.isEmpty()) {
                throw ExceptionFactory.idpException("User creation on IDP failed");
            }
            rep = reps.get(0);
            user.setId(rep.getId());
            //Set the default password
            CredentialRepresentation credRep = new CredentialRepresentation();
            credRep.setType(CredentialRepresentation.PASSWORD);
            credRep.setValue(config.getDefaultNewUserPassword());
            credRep.setTemporary(true);
            users.get(rep.getId()).resetPassword(credRep);
        } else {
            if (reps.size() > 1) {
                throw ExceptionFactory.idpException("More than one use found on IDP");
            }
            user.setId(reps.get(0).getId());
            log.info("User already existed on IDP: {}", user.getEmail());
        }
        return user;
    }

    @Override
    public void deleteUser(UserBean user) {
        UsersResource users = createKeycloakClient().realm(config.getIdpRealm()).users();
        try {

            UserResource u = users.get(user.getId());
            if (u != null) users.delete(user.getId());
        } catch (Exception ex) {
            log.error("Error deleting user from IDP: {}", ex);
            throw ExceptionFactory.idpException("Failure to delete user from IDP");
        }
    }

    private Keycloak createKeycloakClient() {
        return KeycloakBuilder.builder()
                .serverUrl(config.getIdpServerUrl())
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .realm(config.getIdpRealm())
                .clientId(config.getIdpAdminClientId())
                .resteasyClient(new ResteasyClientBuilder().connectionPoolSize(10)
                        .build())
                .clientSecret(config.getIdpAdminClientSecret())
                .build();
    }


}