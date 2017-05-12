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

    @Test
    public void convertHoursToMinutesRoundMiddle() throws Exception {
        long roundMiddle = DateUtils.convertHoursToMinutes(1.075);
        long expected = 65;
        Assert.assertEquals(expected,roundMiddle);
    }

    @Test(expected = NullPointerException.class)
    public void convertHoursToMinutesRoundnull() throws Exception {
        long roundNull = DateUtils.convertHoursToMinutes(null);
    }

    @Test
    public void convertHoursToMinutesRoundMax() throws Exception {
        long roundMiddle = DateUtils.convertHoursToMinutes((double) Long.MAX_VALUE);
        long expected = Long.MAX_VALUE;
        Assert.assertEquals(expected,roundMiddle);
    }

    @Test
    public void convertHoursToMinutesRoundMin() throws Exception {
        long roundMiddle = DateUtils.convertHoursToMinutes((double) Long.MIN_VALUE);
        long expected = Long.MIN_VALUE;
        Assert.assertEquals(expected,roundMiddle);
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