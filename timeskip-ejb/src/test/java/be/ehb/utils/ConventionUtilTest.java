package be.ehb.utils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 *
 * @author Christophe
 * @since 2017
 */
public class ConventionUtilTest {
    @Test
    public void idFromName() {
        String name = "CHRiStopHE";
        String expected = "christophe";
        assertEquals(expected, ConventionUtil.idFromName(name));
    }


    @Test
    public void idFromNameNull() {
        assertEquals(null, ConventionUtil.idFromName(null));
    }


    @Test
    public void idFromNameWhiteSpace() {
        String name = "  ";
        assertEquals(null, ConventionUtil.idFromName(name));
    }

    @Test
    public void idFromNameWithNumbers() {
        String name = "123";
        String expected = "123";
        assertEquals(expected, ConventionUtil.idFromName(name));
    }


}