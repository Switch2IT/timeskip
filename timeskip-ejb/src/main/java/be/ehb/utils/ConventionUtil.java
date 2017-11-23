package be.ehb.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public final class ConventionUtil {

    private ConventionUtil() {
    }

    public static String idFromName(String name) {
        if (StringUtils.isNotEmpty(name)) {
            return name.trim().replaceAll("[^\\w-\\.]", "").toLowerCase();
        } else {
            return null;
        }
    }

}