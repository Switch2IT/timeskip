package be.ehb.utils;

import be.ehb.exceptions.InvalidDateException;
import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * Created by Christophe on 12/05/2017.
 */
public class DateUtilsTest {

    @Test
    public void convertStringToDate() throws Exception {
        String dateToParse = "2017-10-05";
        Date date = new GregorianCalendar(2017, Calendar.OCTOBER,05).getTime();
        LocalDate parsedLocalDate =  LocalDate.fromDateFields(date);
        Assert.assertEquals(parsedLocalDate, DateUtils.convertStringToDate(dateToParse));
    }

    @Test(expected = InvalidDateException.class)
    public void convertStringToDateWrongFormat() throws Exception {
        String dateToParse = "05-10-2017";
        Date date = new GregorianCalendar(2017, Calendar.OCTOBER,05).getTime();
        LocalDate parsedLocalDate =  LocalDate.fromDateFields(date);
        DateUtils.convertStringToDate(dateToParse);
    }

    @Test(expected = InvalidDateException.class)
    public void convertStringToDateNoDate() throws Exception {
        String dateToParse = "";
        Date date = new GregorianCalendar(2017, Calendar.OCTOBER,05).getTime();
        LocalDate parsedLocalDate =  LocalDate.fromDateFields(date);
        DateUtils.convertStringToDate(dateToParse);
    }

    @Test(expected = InvalidDateException.class)
    public void convertStringToDateRightFormatButNotRightDate() throws Exception {
        String dateToParse = "13-13-2017";
        Date date = new GregorianCalendar(2017, 14,05).getTime();
        LocalDate parsedLocalDate =  LocalDate.fromDateFields(date);
        DateUtils.convertStringToDate(dateToParse);
    }

    @Test
    public void convertHoursToMinutesRoundUp() throws Exception {
        long roundUp = DateUtils.convertHoursToMinutes(1.03);
        long expected = 62;
        Assert.assertEquals(expected, roundUp);
    }

    @Test
    public void convertHoursToMinutesRoundDown() throws Exception {
        long roundDown = DateUtils.convertHoursToMinutes(1.02);
        long expected = 61;
        Assert.assertEquals(expected, roundDown);
    }

    @Test
    public void convertHoursToMinutesRoundMiddle() throws Exception {
        long roundMiddle = DateUtils.convertHoursToMinutes(1.075);
        long expected = 65;
        Assert.assertEquals(expected, roundMiddle);
    }

    @Test
    public void convertHoursToMinutesRoundMax() throws Exception {
        long roundMiddle = DateUtils.convertHoursToMinutes((double) Long.MAX_VALUE);
        long expected = Long.MAX_VALUE;
        Assert.assertEquals(expected, roundMiddle);
    }

    @Test
    public void convertHoursToMinutesRoundMin() throws Exception {
        long roundMiddle = DateUtils.convertHoursToMinutes((double) Long.MIN_VALUE);
        long expected = Long.MIN_VALUE;
        Assert.assertEquals(expected, roundMiddle);
    }

    @Test
    public void getDatesBetween() throws Exception {

        String startDate = "2017-10-05";
        String endDate = "2017-10-07";
        List<Date> datesBetween = DateUtils.getDatesBetween(startDate,endDate);
        List<Date> datesBetweenManual = new ArrayList<>();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date start = format.parse(startDate);
        Date between = format.parse("2017-10-06");
        Date after = format.parse(endDate);
        datesBetweenManual.add(start);
        datesBetweenManual.add(between);
        datesBetweenManual.add(after);
        Assert.assertArrayEquals(datesBetween.toArray(),datesBetweenManual.toArray());
        Assert.assertEquals(datesBetween.toArray()[0],datesBetweenManual.toArray()[0]);
        Assert.assertEquals(datesBetween.toArray()[1],datesBetweenManual.toArray()[1]);
    }


    @Test(expected = InvalidDateException.class)
    public void getDatesBetweenWrongDate() throws Exception {
        String startDate = "05-10-2017";
        String endDate = "13-13-2017";
        List<Date> datesBetween = DateUtils.getDatesBetween(startDate,endDate);
    }

    @Test(expected = InvalidDateException.class)
    public void getDatesBetweenStartAfterEnddate() throws Exception {

        String endDate = "2017-10-05";
        String startDate = "2017-10-07";
        List<Date> datesBetween = DateUtils.getDatesBetween(startDate,endDate);
    }

    @Test
    public void convertDateToString() throws Exception {
        String parsedDate = "2017-10-05";
        Date date = new GregorianCalendar(2017, Calendar.OCTOBER,05).getTime();
        LocalDate localDateToParse =  LocalDate.fromDateFields(date);
        Assert.assertEquals(parsedDate,DateUtils.convertDateToString(localDateToParse));
    }

    @Test
    public void convertMinutesToHours() throws Exception {
        long minutes = 150;
        BigDecimal uitkomst = DateUtils.convertMinutesToHours(minutes);
        Assert.assertEquals(new BigDecimal(2.5), uitkomst);
    }

    @Test
    public void convertMinutesToHoursZero() throws Exception {
        long minutes = 0;
        BigDecimal uitkomst = DateUtils.convertMinutesToHours(minutes);
        Assert.assertEquals(BigDecimal.ZERO.setScale(1), uitkomst);
    }

    @Test
    public void convertMinutesToHoursNull() throws Exception {
        BigDecimal uitkomst = DateUtils.convertMinutesToHours(null);
        Assert.assertEquals(BigDecimal.ZERO, uitkomst);
    }

}