package be.ehb.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public class ConventionUtil {

    public static String idFromName(String name) {
        if (StringUtils.isNotEmpty(name)) {
            return name.replaceAll("[^\\w-\\.]", "").toLowerCase();
        } else return null;
    }

}