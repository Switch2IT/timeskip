package be.ehb.exceptions;

import javax.ws.rs.core.Response;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public class NoMembershipException extends AbstractUserException {

    public NoMembershipException(String noMembership) {
        super(noMembership);
    }

    @Override
    public Response.Status getHttpCode() {
        return Response.Status.BAD_REQUEST;
    }

    @Override
    public int getErrorCode() {
        return ErrorCodes.getNoMembership();
    }
}