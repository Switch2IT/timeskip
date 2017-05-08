package be.ehb.factories;

import be.ehb.exceptions.*;
import be.ehb.mail.MailTopic;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public class ExceptionFactory {

    public static UnauthorizedException unauthorizedException(String message) {
        return new UnauthorizedException(message);
    }

    public static UnauthorizedException unauthorizedException(Long entityId) {
        return new UnauthorizedException(entityId.toString());
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

    public static RoleNotFoundException roleNotFoundException(String roleId) {
        return new RoleNotFoundException(roleId);
    }

    public static OrganizationNotFoundException organizationNotFoundException(String organizationId) {
        return new OrganizationNotFoundException(organizationId);
    }

    public static IdpException idpException() {
        return new IdpException();
    }

    public static OrganizationAlreadyExistsException organizationAlreadyExistsException(String organiztionId) {
        return new OrganizationAlreadyExistsException(organiztionId);
    }

    public static ProjectNotFoundException projectNotFoundException(Long projectId) {
        return new ProjectNotFoundException(projectId.toString());
    }

    public static ProjectAlreadyExistsException projectAlreadyExistsException(String projectName) {
        return new ProjectAlreadyExistsException(projectName);
    }

    public static ActivityAlreadyExistsException activityAlreadyExistsException(String name) {
        return new ActivityAlreadyExistsException(name);
    }

    public static ActivityNotFoundException activityNotFoundException(Long id) {
        return new ActivityNotFoundException(id.toString());
    }

    public static InvalidDateException invalidDateException(String message) {
        return new InvalidDateException(message);
    }

    public static NoOverTimeAllowedException noOverTimeAllowedException(String projectName) {
        return new NoOverTimeAllowedException(projectName);
    }

    public static WorklogNotFoundException worklogNotFoundException(Long worklogId) {
        return new WorklogNotFoundException(worklogId.toString());
    }

    public static UserNotAssignedToProjectException userNotAssignedToProjectException(String projectName) {
        return new UserNotAssignedToProjectException(projectName);
    }

    public static MailTemplateNotFoundException mailTemplateNotFoundException(MailTopic topic) {
        return new MailTemplateNotFoundException(topic.toString());
    }

    public static MembershipNotFoundException membershipNotFoundException(Long id) {
        return new MembershipNotFoundException(id.toString());
    }

    public static MailServiceException mailServiceException(String message) {
        return new MailServiceException(message);
    }

    public static PaygradeNotFoundException paygradeNotFoundException(Long paygradeId) {
        return new PaygradeNotFoundException(paygradeId.toString());
    }

    public static PaygradeAlreadyExistsException paygradeAlreadyExists(String name) {
        return new PaygradeAlreadyExistsException(name);
    }

    public static NoUserContextException noUserContextException() {
        return new NoUserContextException();
    }

    public static UserAlreadyExistsException userAlreadyExists(String email) {
        return new UserAlreadyExistsException(email);
    }
}