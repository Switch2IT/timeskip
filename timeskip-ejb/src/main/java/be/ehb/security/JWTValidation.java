package be.ehb.security;

import be.ehb.configuration.IAppConfig;
import be.ehb.factories.ExceptionFactory;
import be.ehb.security.idp.IIdpClient;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.jwt.consumer.JwtContext;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import java.security.Key;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
@ApplicationScoped
@Default
public class JWTValidation {

    @Inject
    private IAppConfig config;
    @Inject
    private IIdpClient idpClient;

    public JwtContext getUnvalidatedContext(String jwt) throws InvalidJwtException, MalformedClaimException {
        return new JwtConsumerBuilder()
                .setSkipAllValidators()
                .setDisableRequireSignature()
                .setSkipSignatureVerification()
                .build()
                .process(jwt);
    }

    public JwtContext getValidatedContext(String jwt) throws InvalidJwtException, MalformedClaimException {
        Key publicKey = idpClient.getPublicKey(config.getIdpRealm(), config.getIdpKeystoreId());
        if (publicKey != null) {
            return new JwtConsumerBuilder()
                    .setAllowedClockSkewInSeconds(10)
                    .setExpectedAudience(config.getIdpClient())
                    .setVerificationKey(idpClient.getPublicKey(config.getIdpRealm(), config.getIdpKeystoreId()))
                    .build()
                    .process(jwt);
        } else {
            throw ExceptionFactory.jwtValidationException("Could not retrieve public key for signature validation");
        }
    }

}