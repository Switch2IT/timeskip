package be.ehb.exceptions;

/**
 * @author Guillaume Vandecasteele/Patrick Van den Bussche
 * @since 2017
 */
class ErrorCodes {

    // ACTIVITY RELATED

    static final int ACTIVITY_NOT_FOUND = 6004;
    static final int ACTIVITY_ALREADY_EXISTS = 6009;

    // IDP RELATED

    static final int IDP_ERROR = 4000;

    // ORGANIZATION RELATED

    static final int UNAUTHORIZED_FOR_ORGANIZATION = 1003;
    static final int ORGANIZATION_NOT_FOUND = 1004;
    static final int ORGANIZATION_ALREADY_EXISTS = 1005;

    // PROJECT RELATED

    static final int PROJECT_NOT_FOUND = 5004;
    static final int PROJECT_ALREADY_EXISTS = 5009;
    static final int PROJEC_OVERTIME_EXCEEDED = 5000;

    // ROLE RELATED

    static final int ROLE_NOT_FOUND = 3001;

    // USER RELATED

    static final int USER_NOT_FOUND = 2001;
    static final int JWT_VALIDATION_ERROR = 2002;
    static final int USER_NOT_ASSIGNED_TO_PROJECT = 2003;

    // WORKLOG RELATED

    static final int WORKLOG_NOT_FOUND = 8004;

    // SYSTEM RELATED

    static final int SYSTEM_ERROR = -1;
    static final int INVALID_DATE = 7001;

    // SCHEDULER RELATED

    static final int SCHEDULER_NOT_FOUND = 7000;
    static final int SCHEDULER_UNABLE_TO_ADD_JOB = 7001;
    static final int SCHEDULER_UNABLE_TO_START = 7002;
    static final int SCHEDULER_UNABLE_TO_SCHEDUULE = 7003;

}