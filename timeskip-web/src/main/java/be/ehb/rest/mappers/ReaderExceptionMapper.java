package be.ehb.rest.mappers;

import be.ehb.exceptions.ErrorCodes;
import be.ehb.factories.ResponseFactory;
import be.ehb.i18n.Messages;
import be.ehb.model.responses.ErrorResponse;
import org.apache.commons.lang3.StringUtils;
import org.jboss.resteasy.spi.ReaderException;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
@Provider
@ApplicationScoped
public class ReaderExceptionMapper implements ExceptionMapper<ReaderException> {

    private static final String ENUM = "Enum";
    private static final String START_ENUM = "instance names: [";
    private static final String END_ENUM = "]\n";

    /**
     * @see ExceptionMapper#toResponse(Throwable)
     */
    @Override
    public Response toResponse(ReaderException data) {
        ErrorResponse error = new ErrorResponse();
        if (data.getCause() != null && StringUtils.isNotEmpty(data.getCause().getMessage()) && data.getCause().getMessage().contains(ENUM)) {
            String originalMessage = data.getCause().getMessage();
            String expectedValues = originalMessage.substring(originalMessage.indexOf(START_ENUM) + 17, originalMessage.indexOf(END_ENUM));
            error.setMessage(Messages.i18n.format("wrongEnumValue", expectedValues));
        } else {
            error.setMessage(Messages.i18n.format("invalidRequestBody"));
        }
        data.printStackTrace();
        error.setHttpCode(Response.Status.BAD_REQUEST.getStatusCode());
        error.setErrorCode(ErrorCodes.INVALID_INPUT);
        return ResponseFactory.buildResponse(Response.Status.BAD_REQUEST, "X-Timeskip-Error", "true", error, MediaType.APPLICATION_JSON);
    }
}