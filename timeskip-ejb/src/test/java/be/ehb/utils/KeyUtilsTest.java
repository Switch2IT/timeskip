package be.ehb.utils;

import org.junit.Test;

import java.security.PublicKey;

import static org.junit.Assert.assertEquals;

/**
 * Created by Christophe on 20/05/2017.
 */
public class KeyUtilsTest {

    @Test
    public void getPublicKeyWhiteSpace() throws Exception {
        String whiteSpace = " ";
        PublicKey value = KeyUtils.getPublicKey(whiteSpace);
        assertEquals(null, value);
    }

    @Test
    public void getPublicKeyNull() throws Exception {
        PublicKey value = KeyUtils.getPublicKey(null);
        assertEquals(null, value);
    }

}