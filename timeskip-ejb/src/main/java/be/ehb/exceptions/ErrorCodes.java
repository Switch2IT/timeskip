package be.ehb.exceptions;

/**
 * @author Guillaume Vandecasteele/Patrick Van den Bussche
 * @since 2017
 */
public class ErrorCodes {

    // ORGANIZATION RELATED

    private static final int UNAUTHORIZED_FOR_ORGANIZATION = 1003;
    private static final int ORGANIZATION_NOT_FOUND = 1004;
    private static final int ORGANIZATION_ALREADY_EXISTS = 1005;

    // USER RELATED

    private static final int USER_NOT_FOUND = 2001;
    private static final int JWT_VALIDATION_ERROR = 2002;
    private static final int NO_USER_CONTEXT_FOUND = 2003;
    private static final int USER_NOT_ASSIGNED_TO_PROJECT = 2004;
    private static final int USER_ALREADY_EXISTS = 2009;

    // ROLE RELATED

    private static final int ROLE_NOT_FOUND = 3001;
    private static final int ROLE_ALREADY_EXISTS = 3009;
    private static final int ROLE_STILL_IN_USE = 3010;

    // IDP RELATED

    private static final int IDP_ERROR = 4000;

    // PAYGRADE RELATED

    private static final int PAYGRADE_NOT_FOUND = 5004;
    private static final int PAYGRADE_ALREADY_EXISTS = 5009;
    private static final int PAYGRADE_STILL_IN_USE = 5010;

    // ACTIVITY RELATED

    private static final int ACTIVITY_NOT_FOUND = 6004;
    private static final int ACTIVITY_ALREADY_EXISTS = 6009;

    // SCHEDULER RELATED

    private static final int SCHEDULER_NOT_FOUND = 7000;
    private static final int SCHEDULER_UNABLE_TO_START = 7001;
    private static final int SCHEDULER_DEPENDENCIES_MISSING = 7002;

    // MAIL RELATED

    private static final int MAIL_SERVICE_ERROR = 8000;

    // OTHER

    private static final int INVALID_DATE = 9001;
    private static final int INVALID_BACKUP = 9002;
    private static final int INVALID_INPUT = 9999;

    // MAIL TEMPLATE RELATED

    private static final int MAIL_TEMPLATE_NOT_FOUND = 10004;

    // MEMBERSHIP RELATED

    private static final int NO_MEMBERSHIP = 11003;
    private static final int MEMBERSHIP_NOT_FOUND = 11004;

    // PROJECT RELATED

    private static final int PROJEC_OVERTIME_EXCEEDED = 12001;
    private static final int PROJECT_NOT_FOUND = 12004;
    private static final int PROJECT_ALREADY_EXISTS = 12009;

    // WORKLOG RELATED

    private static final int WORKLOG_NOT_FOUND = 13004;

    // SYSTEM RELATED
    private static final int SYSTEM_ERROR = -1;

    public static int getUnauthorizedForOrganization() {
        return UNAUTHORIZED_FOR_ORGANIZATION;
    }

    public static int getOrganizationNotFound() {
        return ORGANIZATION_NOT_FOUND;
    }

    public static int getOrganizationAlreadyExists() {
        return ORGANIZATION_ALREADY_EXISTS;
    }

    public static int getActivityNotFound() {
        return ACTIVITY_NOT_FOUND;
    }

    public static int getActivityAlreadyExists() {
        return ACTIVITY_ALREADY_EXISTS;
    }

    public static int getIdpError() {
        return IDP_ERROR;
    }

    public static int getMailServiceError() {
        return MAIL_SERVICE_ERROR;
    }

    public static int getMailTemplateNotFound() {
        return MAIL_TEMPLATE_NOT_FOUND;
    }

    public static int getNoMembership() {
        return NO_MEMBERSHIP;
    }

    public static int getMembershipNotFound() {
        return MEMBERSHIP_NOT_FOUND;
    }

    public static int getPaygradeNotFound() {
        return PAYGRADE_NOT_FOUND;
    }

    public static int getPaygradeAlreadyExists() {
        return PAYGRADE_ALREADY_EXISTS;
    }

    public static int getPaygradeStillInUse() {
        return PAYGRADE_STILL_IN_USE;
    }

    public static int getProjecOvertimeExceeded() {
        return PROJEC_OVERTIME_EXCEEDED;
    }

    public static int getProjectNotFound() {
        return PROJECT_NOT_FOUND;
    }

    public static int getProjectAlreadyExists() {
        return PROJECT_ALREADY_EXISTS;
    }

    public static int getRoleNotFound() {
        return ROLE_NOT_FOUND;
    }

    public static int getRoleAlreadyExists() {
        return ROLE_ALREADY_EXISTS;
    }

    public static int getRoleStillInUse() {
        return ROLE_STILL_IN_USE;
    }

    public static int getUserNotFound() {
        return USER_NOT_FOUND;
    }

    public static int getJwtValidationError() {
        return JWT_VALIDATION_ERROR;
    }

    public static int getNoUserContextFound() {
        return NO_USER_CONTEXT_FOUND;
    }

    public static int getUserNotAssignedToProject() {
        return USER_NOT_ASSIGNED_TO_PROJECT;
    }

    public static int getUserAlreadyExists() {
        return USER_ALREADY_EXISTS;
    }

    public static int getWorklogNotFound() {
        return WORKLOG_NOT_FOUND;
    }

    public static int getSystemError() {
        return SYSTEM_ERROR;
    }

    public static int getInvalidDate() {
        return INVALID_DATE;
    }

    public static int getInvalidBackup() {
        return INVALID_BACKUP;
    }

    public static int getInvalidInput() {
        return INVALID_INPUT;
    }

    public static int getSchedulerNotFound() {
        return SCHEDULER_NOT_FOUND;
    }

    public static int getSchedulerUnableToStart() {
        return SCHEDULER_UNABLE_TO_START;
    }

    public static int getSchedulerDependenciesMissing() {
        return SCHEDULER_DEPENDENCIES_MISSING;
    }
}