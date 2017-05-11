package be.ehb.utils;

import be.ehb.factories.ExceptionFactory;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public class DateUtils {

    private static final String DATE_FORMAT = "dd-MM-yyyy";

    public static LocalDate convertStringToDate(String dateString) {
        try {
            return LocalDate.parse("05-10-2017", DateTimeFormat.forPattern("dd-MM-yyyy"));
        } catch (Exception ex) {
            throw ExceptionFactory.invalidDateException(String.format("Date should have \"%s\" format", DATE_FORMAT));
        }
    }

    public static Long convertHoursToMinutes(Double hours) {
        return Math.round(hours * 60);
    }

    public static List<LocalDate> getDatesBetween(String from, String to) {
        LocalDate startDate = convertStringToDate(from);
        int days = Days.daysBetween(startDate, convertStringToDate(to)).getDays();
        List<LocalDate> dates = new ArrayList<>();
        for (int i = 0; i < days; i++) {
            LocalDate temp = startDate.plusDays(i);
            dates.add(temp);
        }
        return dates;
    }

    public static String convertDateToString(LocalDate day) {
        return day.toString(DATE_FORMAT);
    }
}