package be.ehb.factories;

import be.ehb.exceptions.*;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


/**
 * Created by Christophe on 20/05/2017.
 */
public class ExceptionFactoryTest {

    @Test
    public void unauthorizedExceptionMessage() throws Exception {

        UnauthorizedException unauthorizedException = null;
        String expected = "You do not have authorization to access the following resource: test.";
        try {
            unauthorizedException = ExceptionFactory.unauthorizedException("test");
        } catch (Exception ex) {
        }
        assertEquals(UnauthorizedException.class, unauthorizedException.getClass());
        assertEquals(expected, unauthorizedException.getMessage());
    }

    @Test
    public void unauthorizedException() throws Exception {
        UnauthorizedException unauthorizedException = null;
        String expected;
        try {
            unauthorizedException = ExceptionFactory.unauthorizedException();
        } catch (Exception ex) {
        }
        assertEquals(UnauthorizedException.class, unauthorizedException.getClass());
        assertEquals(null, unauthorizedException.getMessage());
    }

    @Test
    public void unauthorizedExceptionEntityId() throws Exception {
        UnauthorizedException unauthorizedException = null;
        String expected = "You do not have authorization to access the following resource: 10.";
        try {
            unauthorizedException = ExceptionFactory.unauthorizedException(10L);
        } catch (Exception ex) {
        }
        assertEquals(UnauthorizedException.class, unauthorizedException.getClass());
        assertEquals(expected, unauthorizedException.getMessage());
    }

    @Test
    public void systemErrorException() throws Exception {
        SystemErrorException systemErrorException = null;
        String expected = "Test";
        try {
            systemErrorException = ExceptionFactory.systemErrorException("Test");
        } catch (Exception ex) {
        }
        assertEquals(SystemErrorException.class, systemErrorException.getClass());
        assertEquals(expected, systemErrorException.getMessage());
    }

    @Test
    public void systemErrorExceptionThrowableCause() throws Exception {
        Throwable throwable = new Throwable();
        SystemErrorException systemErrorException = null;
        try {
            systemErrorException = ExceptionFactory.systemErrorException(throwable);
        } catch (Exception ex) {
        }
        assertEquals(SystemErrorException.class, systemErrorException.getClass());
        assertEquals(throwable, systemErrorException.getCause());
    }

    @Test
    public void defaultConfigNotFoundException() throws Exception {
        DefaultConfigNotFoundException defaultConfigNotFoundException = null;
        try {
            defaultConfigNotFoundException = ExceptionFactory.defaultConfigNotFoundException();
        } catch (Exception ex) {
        }
        assertEquals(DefaultConfigNotFoundException.class, defaultConfigNotFoundException.getClass());
    }

    @Test
    public void userNotFoundException() throws Exception {
        UserNotFoundException userNotFoundException = null;
        String userId = "Christophe";
        try {
            userNotFoundException = ExceptionFactory.userNotFoundException(userId);
        } catch (Exception ex) {
        }
        assertEquals(UserNotFoundException.class, userNotFoundException.getClass());
        assertEquals(userId, userNotFoundException.getMessage());

    }

    @Test
    public void jwtValidationException() throws Exception {
        JwtValidationException jwtValidationException = null;
        String messsage = "Christophe";
        try {
            jwtValidationException = ExceptionFactory.jwtValidationException(messsage);
        } catch (Exception ex) {
        }
        assertEquals(JwtValidationException.class, jwtValidationException.getClass());
        assertEquals(messsage, jwtValidationException.getMessage());

    }

    @Test
    public void roleNotFoundException() throws Exception {
    }

    @Test
    public void organizationNotFoundException() throws Exception {
    }

    @Test
    public void idpException() throws Exception {
    }

    @Test
    public void organizationAlreadyExistsException() throws Exception {
    }

    @Test
    public void projectNotFoundException() throws Exception {
    }

    @Test
    public void projectAlreadyExistsException() throws Exception {
    }

    @Test
    public void activityAlreadyExistsException() throws Exception {
    }

    @Test
    public void activityNotFoundException() throws Exception {
    }

    @Test
    public void schedulerNotFoundException() throws Exception {
    }

    @Test
    public void schedulerUnableToAddJobException() throws Exception {
    }

    @Test
    public void schedulerUnableToStartException() throws Exception {
    }

    @Test
    public void schedulerUnableToScheduleException() throws Exception {
    }

    @Test
    public void invalidDateException() throws Exception {
    }

    @Test
    public void noOverTimeAllowedException() throws Exception {
    }

    @Test
    public void worklogNotFoundException() throws Exception {
    }

    @Test
    public void userNotAssignedToProjectException() throws Exception {
    }

    @Test
    public void mailTemplateNotFoundException() throws Exception {
    }

    @Test
    public void membershipNotFoundException() throws Exception {
    }

    @Test
    public void mailServiceException() throws Exception {
    }

    @Test
    public void paygradeNotFoundException() throws Exception {
    }

    @Test
    public void paygradeAlreadyExists() throws Exception {
    }

    @Test
    public void noUserContextException() throws Exception {
    }

    @Test
    public void userAlreadyExists() throws Exception {
    }

    @Test
    public void invalidBackupDataException() throws Exception {
    }

    @Test
    public void unavailableException() throws Exception {
    }

    @Test
    public void roleAlreadyExistsException() throws Exception {
    }

    @Test
    public void roleStillInUseException() throws Exception {
    }

    @Test
    public void paygradeStillInUseException() throws Exception {
    }

    @Test
    public void noMembershipException() throws Exception {
    }

}