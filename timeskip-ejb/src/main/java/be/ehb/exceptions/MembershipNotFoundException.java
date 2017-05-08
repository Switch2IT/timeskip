package be.ehb.exceptions;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public class MembershipNotFoundException extends AbstractNotFoundException {

    public MembershipNotFoundException(String message) {
        super(message);
    }

    public MembershipNotFoundException(Throwable cause) {
        super(cause);
    }

    @Override
    public int getErrorCode() {
        return ErrorCodes.MEMBERSHIP_NOT_FOUND;
    }
}