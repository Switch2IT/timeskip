package be.ehb.exceptions;

import org.apache.log4j.spi.ErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public class OrganizationAlreadyExistsException extends AbstractAlreadyExistsException {

    public OrganizationAlreadyExistsException(String message) {
        super(message);
    }

    @Override
    public int getErrorCode() {
        return ErrorCodes.ORGANIZATION_ALREADY_EXISTS;
    }
}