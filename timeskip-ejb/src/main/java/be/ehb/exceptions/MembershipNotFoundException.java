package be.ehb.exceptions;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public class MembershipNotFoundException extends AbstractNotFoundException {

    public MembershipNotFoundException(String message) {
        super(message);
    }

    @Override
    public int getErrorCode() {
        return ErrorCodes.getMembershipNotFound();
    }
}