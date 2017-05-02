package be.ehb.servlets;

import be.ehb.configuration.AppConfig;
import be.ehb.security.ISecurityContext;
import be.ehb.security.JWTValidation;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import java.io.IOException;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public class RequestFilter implements ContainerRequestFilter {


    private static final Logger log = LoggerFactory.getLogger(be.ehb.servlets.RequestFilter.class);

    private static final String HEADER_USER_AUTHORIZATION = "Authorization"; // will contain the JWT user token

    //Exclusions
    private static final String BASE_PATH = "/timeskip-web";
    private static final String SYSTEM_PATH = "/system";
    @Inject
    private ISecurityContext securityContext;
    @Inject
    private AppConfig config;
    @Inject
    private JWTValidation jwtValidation;



    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {
        log.debug("Security context - request:{}", containerRequestContext.getUriInfo().getRequestUri().getPath());
        String path = containerRequestContext.getUriInfo().getRequestUri().getPath();
        //Filter requests to intercept JWT. Allow calls to system info
        if (!path.startsWith(BASE_PATH + SYSTEM_PATH)) {
            String jwt = containerRequestContext.getHeaderString(HEADER_USER_AUTHORIZATION);
            if (jwt != null) {
                //remove Bearer prefix
                jwt = jwt.replaceFirst("Bearer", "").trim();
                String validatedUser = "";
                try {
                    JwtClaims jwtClaims = jwtValidation.getValidatedContext(jwt).getJwtClaims();
                    validatedUser = securityContext.setCurrentUser(jwtClaims);
                } catch (InvalidJwtException | MalformedClaimException ex) {

                    log.error("Unauthorized user:{}", validatedUser);
                    containerRequestContext.abortWith(Response
                            .status(Response.Status.UNAUTHORIZED)
                            .entity("User cannot access the resource:" + validatedUser)
                            .build());
                    return;
                }
            } else {
                securityContext.setCurrentUser("");
            }
        }
    }
}