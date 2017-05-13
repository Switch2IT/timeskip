package be.ehb.exceptions;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public class ErrorCodes {

    // ACTIVITY RELATED

    public static final int INVALID_INPUT = 9999;
    static final int ACTIVITY_NOT_FOUND = 6004;

    // IDP RELATED

    static final int ACTIVITY_ALREADY_EXISTS = 6009;

    // MAIL RELATED

    static final int IDP_ERROR = 4000;
    static final int MAIL_SERVICE_ERROR = 9000;

    // MEMBERSHIP RELATED

    static final int MAIL_TEMPLATE_NOT_FOUND = 9004;

    // ORGANIZATION RELATED

    static final int MEMBERSHIP_NOT_FOUND = 10004;
    static final int UNAUTHORIZED_FOR_ORGANIZATION = 1003;
    static final int ORGANIZATION_NOT_FOUND = 1004;

    // PAYGRADE RELATED

    static final int PAYGRADE_NOT_FOUND = 5004;
    static final int PAYGRADE_ALREADY_EXISTS = 5009;

    // PROJECT RELATED
    static final int ORGANIZATION_ALREADY_EXISTS = 1005;
    static final int PROJECT_NOT_FOUND = 5004;
    static final int PROJECT_ALREADY_EXISTS = 5009;

    // ROLE RELATED
    static final int PROJEC_OVERTIME_EXCEEDED = 3000;
    static final int ROLE_NOT_FOUND = 3001;
    static final int ROLE_ALREADY_EXISTS = 3009;
    static final int ROLE_STILL_IN_USE = 3010;


    // USER RELATED

    static final int USER_NOT_FOUND = 2001;
    static final int JWT_VALIDATION_ERROR = 2002;
    static final int NO_USER_CONTEXT_FOUND = 2003;
    static final int USER_ALREADY_EXISTS = 2009;

    // WORKLOG RELATED

    static final int USER_NOT_ASSIGNED_TO_PROJECT = 2003;

    // SYSTEM RELATED

    static final int FUNCTIONALITY_UNAVAILABLE = 8003;
    static final int WORKLOG_NOT_FOUND = 8004;
    static final int SYSTEM_ERROR = -1;

    // OTHER

    static final int INVALID_DATE = 7001;
    static final int INVALID_BACKUP = 7002;
}