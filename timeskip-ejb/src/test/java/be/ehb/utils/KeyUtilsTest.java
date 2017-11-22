package be.ehb.utils;

import org.junit.Test;

import java.security.PublicKey;

import static org.junit.Assert.assertEquals;

/**
 *
 * @author Christophe
 * @since 2017
 */
public class KeyUtilsTest {

    @Test
    public void getPublicKeyWhiteSpace() {
        String whiteSpace = " ";
        PublicKey value = KeyUtils.getPublicKey(whiteSpace);
        assertEquals(null, value);
    }

    @Test
    public void getPublicKeyNull() {
        PublicKey value = KeyUtils.getPublicKey(null);
        assertEquals(null, value);
    }

}