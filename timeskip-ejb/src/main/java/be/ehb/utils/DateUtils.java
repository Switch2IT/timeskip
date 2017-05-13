package be.ehb.utils;

import be.ehb.factories.ExceptionFactory;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public class DateUtils {

    private static final String DATE_FORMAT = "yyyy-MM-dd";

    public static LocalDate convertStringToDate(String dateString) {
        try {
            return LocalDate.parse(dateString, DateTimeFormat.forPattern(DATE_FORMAT));
        } catch (Exception ex) {
            throw ExceptionFactory.invalidDateException(String.format("Date should have \"%s\" format", DATE_FORMAT));
        }
    }

    public static Long convertHoursToMinutes(Double hours) {
        try {
            return Math.round(hours * 60);
        } catch (NullPointerException ex) {
            return 0L;
        }
    }

    public static List<Date> getDatesBetween(String from, String to) {
        LocalDate startDate = convertStringToDate(from);
        int days = Days.daysBetween(startDate, convertStringToDate(to)).getDays();
        List<Date> dates = new ArrayList<>();
        for (int i = 0; i < days; i++) {
            LocalDate temp = startDate.plusDays(i);
            dates.add(temp.toDate());
        }
        return dates;
    }

    public static String convertDateToString(LocalDate day) {
        return day.toString(DATE_FORMAT);
    }

    public static BigDecimal convertMinutesToHours(Long minutes) {
        try {
            return new BigDecimal(minutes.doubleValue() / 60).setScale(1, BigDecimal.ROUND_HALF_UP);
        } catch (NullPointerException ex) {
            return BigDecimal.ZERO;
        }
    }
}