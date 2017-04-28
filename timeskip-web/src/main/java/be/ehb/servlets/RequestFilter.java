package be.ehb.servlets;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import java.io.IOException;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public class RequestFilter implements ContainerRequestFilter {


    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        //TODO - Set up the user context based on Authorization header here
    }
}