package be.ehb.servlets;

import be.ehb.configuration.IAppConfig;
import be.ehb.factories.ResponseFactory;
import be.ehb.i18n.Messages;
import be.ehb.model.responses.ErrorResponse;
import be.ehb.security.ISecurityContext;
import be.ehb.security.JWTValidation;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import java.io.IOException;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
@ApplicationScoped
public class RequestFilter implements ContainerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(RequestFilter.class);

    private static final String HEADER_USER_AUTHORIZATION = "Authorization";
    private static final String KEYCLOAK_TOKEN = "keycloak-token";
    private static final String ACCESS_TOKEN = "access_token";

    //Exclusions
    private static final String BASE_PATH = "/timeskip-web/api";
    private static final String SWAGGER_DOC_JSON = "/swagger.json";
    private static final String SYSTEM_PATH = "/system/status";

    @Inject
    private ISecurityContext securityContext;
    @Inject
    private IAppConfig config;
    @Inject
    private JWTValidation jwtValidation;

    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {
        log.debug("Security context - request:{}", containerRequestContext.getUriInfo().getRequestUri().getPath());
        String path = containerRequestContext.getUriInfo().getRequestUri().getPath();
        //Filter requests to intercept JWT. Allow calls to system info
        if (!(path.startsWith(BASE_PATH + SYSTEM_PATH) || path.startsWith(BASE_PATH + SWAGGER_DOC_JSON))) {
            String jwt = null;
            if (containerRequestContext.getHeaders().containsKey(HEADER_USER_AUTHORIZATION)) {
                jwt = containerRequestContext.getHeaderString(HEADER_USER_AUTHORIZATION);
            } else if (containerRequestContext.getHeaders().containsKey(KEYCLOAK_TOKEN)) {
                jwt = containerRequestContext.getHeaderString(KEYCLOAK_TOKEN);
            } else if (containerRequestContext.getHeaders().containsKey(ACCESS_TOKEN)) {
                jwt = containerRequestContext.getHeaderString(ACCESS_TOKEN);
            }
            if (jwt != null) {
                //remove Bearer prefix
                jwt = jwt.replaceFirst("Bearer", "").trim();
                String validatedUser = "";
                try {
                    JwtClaims jwtClaims;
                    if (config.getValidateJWT()) {
                        jwtClaims = jwtValidation.getValidatedContext(jwt).getJwtClaims();
                    } else {
                        jwtClaims = jwtValidation.getUnvalidatedContext(jwt).getJwtClaims();
                    }
                    validatedUser = securityContext.setCurrentUser(jwtClaims);
                } catch (InvalidJwtException | MalformedClaimException ex) {
                    log.error("Unauthorized user:{}", validatedUser);
                    ex.printStackTrace();
                    ErrorResponse err = new ErrorResponse();
                    err.setMessage(Messages.i18n.format("invalidJWT", jwt));
                    err.setHttpCode(Response.Status.UNAUTHORIZED.getStatusCode());
                    containerRequestContext.abortWith(ResponseFactory.buildResponse(Response.Status.UNAUTHORIZED, err));
                }
            } else {
                securityContext.setCurrentUser("");
            }
        }
    }
}