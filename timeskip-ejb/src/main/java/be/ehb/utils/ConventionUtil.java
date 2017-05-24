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
        if (name == null) {
            return null;
        }
        if (StringUtils.isNotEmpty(name.trim())) {
            return name.replaceAll("[^\\w-\\.]", "").toLowerCase();
        } else return null;
    }

}