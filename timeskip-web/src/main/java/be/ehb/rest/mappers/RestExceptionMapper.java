package be.ehb.rest.mappers;

import be.ehb.exceptions.AbstractRestException;
import be.ehb.factories.ResponseFactory;
import be.ehb.model.responses.ErrorResponse;
import org.apache.commons.lang3.StringUtils;

import javax.enterprise.context.ApplicationScoped;
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
        error.setHttpCode(data.getHttpCode().getStatusCode());
        if (StringUtils.isNotEmpty(data.getMessage())) {
            error.setMessage(data.getMessage());
        }
        return ResponseFactory.buildResponse(data.getHttpCode(), "X-Timeskip-Error", "true", error);
    }

}
