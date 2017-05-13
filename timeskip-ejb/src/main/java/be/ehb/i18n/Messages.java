package be.ehb.i18n;

import java.text.MessageFormat;
import java.util.*;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public class Messages {

    public static final Messages i18n = new Messages();

    private static final List<String> FORMATS = Collections.singletonList("java.properties");

    private static Map<String, ResourceBundle> bundles = new HashMap<>();

    private static ThreadLocal<Locale> tlocale = new ThreadLocal<>();


    private ResourceBundle getBundle() {
        String bundleKey = getBundleKey();
        if (bundles.containsKey(bundleKey)) {
            return bundles.get(bundleKey);
        } else {
            ResourceBundle bundle = loadBundle();
            bundles.put(bundleKey, bundle);
            return bundle;
        }
    }

    private String getBundleKey() {
        Locale locale = getLocale();
        return this.getClass().getName() + "::" + locale.toString();
    }

    private ResourceBundle loadBundle() {
        String pkg = this.getClass().getPackage().getName();
        Locale locale = getLocale();
        return PropertyResourceBundle.getBundle(pkg + ".messages", locale, this.getClass().getClassLoader(), new ResourceBundle.Control() {
            @Override
            public List<String> getFormats(String baseName) {
                return FORMATS;
            }
        });
    }

    private Locale getLocale() {
        if (tlocale.get() != null) {
            return tlocale.get();
        } else {
            return Locale.getDefault();
        }
    }

    public String format(String key, Object... params) {
        ResourceBundle bundle = getBundle();
        if (bundle.containsKey(key)) {
            String msg = bundle.getString(key);
            return MessageFormat.format(msg, params);
        } else {
            return MessageFormat.format("!!{0}!!", key); //$NON-NLS-1$
        }
    }

}