package be.ehb.rest.mappers;

import be.ehb.exceptions.ErrorCodes;
import be.ehb.model.responses.ErrorResponse;
import org.apache.commons.lang3.StringUtils;

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
public class NullPointerExceptionMapper implements ExceptionMapper<NullPointerException> {

    /**
     * @see ExceptionMapper#toResponse(Throwable)
     */
    @Override
    public Response toResponse(NullPointerException data) {

        ErrorResponse error = new ErrorResponse();
        if (StringUtils.isNotEmpty(data.getMessage())) {
            error.setMessage(data.getMessage());
        } else {
            //TODO - Use resource bundle for internationalization
            error.setMessage("Missing input");
        }
        error.setErrorCode(ErrorCodes.HTTP_STATUS_CODE_INVALID_INPUT);
        Response.ResponseBuilder builder = Response.status(ErrorCodes.HTTP_STATUS_CODE_INVALID_INPUT).header("X-Timeskip-Error", "true");
        builder.type(MediaType.APPLICATION_JSON_TYPE);
        return builder.entity(error).build();
    }
}