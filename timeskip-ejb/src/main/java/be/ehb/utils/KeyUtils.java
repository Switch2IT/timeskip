package be.ehb.utils;

import org.bouncycastle.util.io.pem.PemReader;

import java.io.IOException;
import java.io.StringReader;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public class KeyUtils {

    public static PublicKey getPublicKey(String pubKey) {
        try {
            PemReader pemReader = new PemReader(new StringReader(pubKey));
            byte[] content = pemReader.readPemObject().getContent();
            X509EncodedKeySpec spec = new X509EncodedKeySpec(content);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePublic(spec);
        } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException ex) {
            return null;
        }
    }
}