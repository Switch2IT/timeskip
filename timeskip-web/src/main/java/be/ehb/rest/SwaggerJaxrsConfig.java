package be.ehb.rest;

import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.models.*;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
@WebServlet(name = "SwaggerJaxrsConfig", loadOnStartup = 1)
public class SwaggerJaxrsConfig extends HttpServlet {

    @Override
    public void init(ServletConfig config) throws ServletException {
        try {
            super.init(config);
            //Scanner
            BeanConfig beanConfig = new BeanConfig();
            beanConfig.setTitle("Timeskip API");
            beanConfig.setVersion("v0.6");
            beanConfig.setBasePath("timeskip-web/api");
            beanConfig.setResourcePackage("be.ehb.rest.resources");
            beanConfig.setScan(true);

            //information
            Info info = new Info()
                    .title("Timeskip API")
                    .description("API for the Timeskip timesheet management backend")
                    .termsOfService("Only use this if authorized")
                    .contact(new Contact().email("guillaume.vandecasteele@student.ehb.be"))
                    .license(new License().name("Timeskip").url("https://www.ehb.be"));
            ServletContext context = config.getServletContext();

            //configuration
            Swagger swagger = new Swagger().info(info);
            swagger.externalDocs(new ExternalDocs("Find out more about the Timeskip API", "https://github.com/Switch2IT/timeskip"));
            swagger.scheme(Scheme.HTTP);
            swagger.host("localhost:8080");
            swagger.basePath("timeskip-web/api");
            context.setAttribute("swagger", swagger);
        } catch (ServletException e) {
            System.out.println(e.getMessage());
        }
    }
}