package be.ehb.servlets;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
@WebFilter(filterName = "CORSFilter", urlPatterns = {"/*"})
public class CORSFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        //No implementation necessary
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse res = (HttpServletResponse) response;
        HttpServletRequest req = (HttpServletRequest) request;
        res.setHeader("Access-Control-Allow-Origin", req.getHeader("Origin"));
        res.setHeader("Access-Control-Allow-Credentials", "true");
        res.addHeader("Access-Control-Allow-Methods", "OPTIONS, GET, POST, DELETE, PUT, HEAD");
        res.addHeader("Access-Control-Allow-Headers", "Content-Type, Authorization, access_token, keycloak-token");
        res.addHeader("Access-Control-Expose-Headers", "X-Timeskip-Error");
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        //No implementation necessary
    }
}