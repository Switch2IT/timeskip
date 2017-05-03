package be.ehb.security.idp;

import java.security.Key;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public interface IIdpClient {
    Key getPublicKey(String realm, String keystoreId);
}
