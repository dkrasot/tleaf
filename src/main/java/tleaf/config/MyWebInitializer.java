package tleaf.config;

import org.springframework.web.context.support.XmlWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;
import tleaf.web.WebConfig;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;


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

// maybe would add XML configs:

// @ImportResource( {"classpath:CONFIG_NAME.xml",...} ) or :

//    @Override
//    public void onStartup(ServletContext servletContext) throws ServletException {
//        XmlWebApplicationContext applicationContext = new XmlWebApplicationContext();
//        applicationContext.setConfigLocation("/WEB-INF/dispatcher-config.xml");
//
//        ServletRegistration.Dynamic dispatcher = servletContext.addServlet("dispatcher", new DispatcherServlet(applicationContext));
//        dispatcher.setLoadOnStartup(1);
//        dispatcher.addMapping("/");
//    }
}