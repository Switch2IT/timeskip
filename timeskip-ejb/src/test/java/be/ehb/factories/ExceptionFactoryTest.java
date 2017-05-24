package be.ehb.factories;

import be.ehb.exceptions.*;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


/**
 *
 * @author Guillaume Vandecasteele/Christophe Devos
 * @since 2017
 */
public class ExceptionFactoryTest {

    @Test
    public void unauthorizedExceptionMessage() {
        String expected = "You do not have authorization to access the following resource: test.";
        UnauthorizedException unauthorizedException = ExceptionFactory.unauthorizedException("test");
        assertEquals(UnauthorizedException.class, unauthorizedException.getClass());
        assertEquals(expected, unauthorizedException.getMessage());
    }

    @Test
    public void unauthorizedException() {
        UnauthorizedException unauthorizedException = ExceptionFactory.unauthorizedException();
        assertEquals(UnauthorizedException.class, unauthorizedException.getClass());
        assertEquals(null, unauthorizedException.getMessage());
    }

    @Test
    public void unauthorizedExceptionEntityId() {
        String expected = "You do not have authorization to access the following resource: 10.";
        UnauthorizedException unauthorizedException = ExceptionFactory.unauthorizedException(10L);
        assertEquals(UnauthorizedException.class, unauthorizedException.getClass());
        assertEquals(expected, unauthorizedException.getMessage());
    }

    @Test
    public void systemErrorException() {
        String expected = "Test";
        SystemErrorException systemErrorException = ExceptionFactory.systemErrorException("Test");
        assertEquals(SystemErrorException.class, systemErrorException.getClass());
        assertEquals(expected, systemErrorException.getMessage());
    }

    @Test
    public void systemErrorExceptionThrowableCause() {
        Throwable throwable = new Throwable();
        SystemErrorException systemErrorException = ExceptionFactory.systemErrorException(throwable);
        assertEquals(SystemErrorException.class, systemErrorException.getClass());
        assertEquals(throwable, systemErrorException.getCause());
    }

    @Test
    public void defaultConfigNotFoundException() {
        DefaultConfigNotFoundException defaultConfigNotFoundException = ExceptionFactory.defaultConfigNotFoundException();
        assertEquals(DefaultConfigNotFoundException.class, defaultConfigNotFoundException.getClass());
    }

    @Test
    public void userNotFoundException() {
        String userId = "Christophe";
        UserNotFoundException userNotFoundException = ExceptionFactory.userNotFoundException(userId);
        assertEquals(UserNotFoundException.class, userNotFoundException.getClass());
        assertEquals(userId, userNotFoundException.getMessage());

    }

    @Test
    public void jwtValidationException() {
        String messsage = "Christophe";
        JwtValidationException jwtValidationException = ExceptionFactory.jwtValidationException(messsage);
        assertEquals(JwtValidationException.class, jwtValidationException.getClass());
        assertEquals(messsage, jwtValidationException.getMessage());

    }
}