package be.ehb.utils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Christophe on 20/05/2017.
 */
public class ConventionUtilTest {
    @Test
    public void idFromName() throws Exception {
        String name = "CHRiStopHE";
        String expected = "christophe";
        assertEquals(expected, ConventionUtil.idFromName(name));
    }


    @Test
    public void idFromNameNull() throws Exception {
        String name;
        String expected;
        assertEquals(null, ConventionUtil.idFromName(null));
    }


    @Test
    public void idFromNameWhiteSpace() throws Exception {
        String name = "  ";
        assertEquals(null, ConventionUtil.idFromName(name));
    }

    @Test
    public void idFromNameWithNumbers() throws Exception {
        String name = "123";
        String expected = "123";
        assertEquals(expected, ConventionUtil.idFromName(name));
    }


}