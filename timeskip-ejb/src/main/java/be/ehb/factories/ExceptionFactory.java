package be.ehb.factories;

import be.ehb.exceptions.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public class ExceptionFactory {

    public static UnauthorizedException unauthorizedException(String entityId) {
        return new UnauthorizedException(entityId);
    }

}