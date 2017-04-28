package be.ehb.utils;

import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.jwt.consumer.JwtContext;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public class JWTUtils {

    public static JwtContext getUnvalidatedContext(String jwt) throws InvalidJwtException, MalformedClaimException {
        return new JwtConsumerBuilder()
                .setSkipAllValidators()
                .setDisableRequireSignature()
                .setSkipSignatureVerification()
                .build()
                .process(jwt);
    }

}