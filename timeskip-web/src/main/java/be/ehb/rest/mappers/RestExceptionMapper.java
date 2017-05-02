package be.ehb.rest.mappers;

import be.ehb.exceptions.AbstractRestException;
import be.ehb.model.responses.ErrorResponse;
import org.apache.commons.lang3.StringUtils;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Provider that maps an error.
 */
@Provider
@ApplicationScoped
public class RestExceptionMapper implements ExceptionMapper<AbstractRestException> {

    @Override
    public Response toResponse(AbstractRestException data) {
        ErrorResponse error = new ErrorResponse();
        error.setType(data.getClass().getSimpleName());
        error.setErrorCode(data.getErrorCode());
        error.setHttpCode(data.getHttpCode());
        if (StringUtils.isNotEmpty(data.getMessage())) {
            error.setMessage(data.getMessage());
        }
        Response.ResponseBuilder builder = Response.status(data.getHttpCode()).header("X-Timeskip-Error", "true");
        builder.type(MediaType.APPLICATION_JSON_TYPE);
        return builder.entity(error).build();
    }

}
