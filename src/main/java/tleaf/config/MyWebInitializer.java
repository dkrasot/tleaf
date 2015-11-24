package tleaf.config;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;
import tleaf.web.WebConfig;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletRegistration;

//Alternative to web.xml - needs Servlet 3.0+
// creates DispatcherServlet (using getServletCC (WebConfig) for creating Spring app ctx)
// and ContextLoaderListener (using getRootCC -> 2nd app ctx )

public class MyWebInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class<?>[]{WebConfig.class};
    }

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class<?>[]{RootConfig.class, SecurityConfig.class};
    }

    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }

    @Override
    protected void customizeRegistration(ServletRegistration.Dynamic registration) {
        registration.setMultipartConfig(
                new MultipartConfigElement("/", 2097152, 4194304, 0)
                // ONLY "/" location is ok for GlassFish; WebSphere is OK with /tmp/some dir/uploads/...
        );
        // registration.setAsyncSupported(true); //for STOMP implementation
    }
    // ServletRegistration.Dynamic supports configuring of:
    // multipart-requests by setMultipartConfig(), load priorities by setLoadOnStartup(), init params by setInitParameter()
}


// TODO : ADD HTML views like in older project D:\Java\ideaProjects\MyThyme\src\main\webapp\WEB-INF\views
