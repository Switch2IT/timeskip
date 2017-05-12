package be.ehb.utils;

import org.junit.*;




/**
 * Created by Christophe on 12/05/2017.
 */
public class DateUtilsTest {
 /*   @Test
    public void convertStringToDate() throws Exception {

    }
*/
    @Test
    public void convertHoursToMinutesRoundUp() throws Exception {
        long roundUp = DateUtils.convertHoursToMinutes(1.03);
        long expected = 62;
        Assert.assertEquals(expected,roundUp);
    }

    @Test
    public void convertHoursToMinutesRoundDown() throws Exception {
        long roundDown = DateUtils.convertHoursToMinutes(1.02);
        long expected = 61;
        Assert.assertEquals(expected,roundDown);
    }
/*
    @Test
    public void getDatesBetween() throws Exception {

    }

    @Test
    public void convertDateToString() throws Exception {

    }
*/
}