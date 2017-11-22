package be.ehb.utils;

import org.bouncycastle.util.encoders.Base64;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public final class KeyUtils {

    private KeyUtils() {
    }

    public static PublicKey getPublicKey(String key) {
        try {
            byte[] byteKey = Base64.decode(key.getBytes());
            //byte[] byteKey = Base64.decode(key.getBytes(), Base64.DEFAULT);
            X509EncodedKeySpec X509publicKey = new X509EncodedKeySpec(byteKey);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePublic(X509publicKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}