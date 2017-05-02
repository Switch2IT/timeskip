package be.ehb.factories;

import be.ehb.exceptions.StorageException;
import be.ehb.exceptions.SystemErrorException;
import be.ehb.exceptions.UnauthorizedException;
import be.ehb.exceptions.UserNotFoundException;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public class ExceptionFactory {

    public static UnauthorizedException unauthorizedException(String entityId) {
        return new UnauthorizedException(entityId);
    }

    public static SystemErrorException systemErrorException(String message) {
        return new SystemErrorException(message);
    }

    public static SystemErrorException systemErrorException(Throwable cause) {
        return new SystemErrorException(cause);
    }

    public static StorageException storageException(String message) {
        return new StorageException(message);
    }

    public static UserNotFoundException userNotFoundException(String userId) {
        return new UserNotFoundException(userId);
    }

}