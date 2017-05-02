package be.ehb.exceptions;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public class ErrorCodes {

    // SYSTEM RELATED

    public static final int SYSTEM_ERROR = -1;

    // HTTP CODES

    public static final int HTTP_STATUS_CODE_INVALID_INPUT  = 400;
    public static final int HTTP_STATUS_CODE_FORBIDDEN      = 403;
    public static final int HTTP_STATUS_CODE_NOT_FOUND      = 404;
    public static final int HTTP_STATUS_CODE_ALREADY_EXISTS = 409;
    public static final int HTTP_STATUS_CODE_INVALID_STATE  = 409;
    public static final int HTTP_STATUS_CODE_SYSTEM_ERROR   = 500;

    // ORGANIZATION RELATED

    public static final int UNAUTHORIZED_FOR_ORGANIZATION   = 1003;

    // USER RELATED

    public static final int USER_NOT_FOUND = 2001;
}