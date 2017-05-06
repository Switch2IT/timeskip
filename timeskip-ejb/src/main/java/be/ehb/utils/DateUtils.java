package be.ehb.utils;

import be.ehb.factories.ExceptionFactory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public class DateUtils {

    private static final String DATE_FORMAT = "dd-MM-yyyy";

    public static Date convertStringToDate(String dateString) {
        try {
            DateFormat df = new SimpleDateFormat(DATE_FORMAT);
            return df.parse(dateString);
        } catch (ParseException ex) {
            throw ExceptionFactory.invalidDateException(String.format("Date should have \"%s\" format", DATE_FORMAT));
        }
    }

    public static Long convertHoursToMinutes(Double hours) {
        return Math.round(hours * 60);
    }

}