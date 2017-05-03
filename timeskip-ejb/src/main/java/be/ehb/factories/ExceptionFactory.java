package be.ehb.factories;

import be.ehb.exceptions.*;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public class ExceptionFactory {

    public static UnauthorizedException unauthorizedException(String entityId) {
        return new UnauthorizedException(entityId);
    }

    public static UnauthorizedException unauthorizedException() {
        return new UnauthorizedException();
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

    public static JwtValidationException jwtValidationException(String message) {
        return new JwtValidationException(message);
    }

}