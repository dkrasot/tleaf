package tleaf.config;

import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletRegistration;


public class MainInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class<?>[]{WebConfig.class};
    }

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class<?>[]{RootConfig.class, SecurityConfig.class, MethodSecurityConfig.class, CachingConfig.class};
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

    //activation of Spring @Profile 'dev' -> TODO later active profile(s) to .properties-file
    //use @ActiveProfiles(...) for test diff configs https://spring.io/blog/2011/06/21/spring-3-1-m2-testing-with-configuration-classes-and-profiles
    @Override
    protected WebApplicationContext createRootApplicationContext() {
        WebApplicationContext context = (WebApplicationContext) super.createRootApplicationContext();
        ((ConfigurableEnvironment)context.getEnvironment()).setActiveProfiles("development");
        return context;
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