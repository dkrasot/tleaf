package tleaf.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;
import org.thymeleaf.extras.springsecurity4.dialect.SpringSecurityDialect;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;
import org.thymeleaf.templateresolver.TemplateResolver;

import java.io.IOException;
import java.util.Properties;

//changed Spring3 and SprSecurity3 to 4 version

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"tleaf.web"})
public class WebConfig extends WebMvcConfigurerAdapter {

    @Bean
    public ViewResolver viewResolver(SpringTemplateEngine templateEngine) {
        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
        viewResolver.setTemplateEngine(templateEngine);
        return viewResolver;
    }

    @Bean
    public SpringTemplateEngine templateEngine(TemplateResolver templateResolver) {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);
        // enabling Spring Security in Thymeleaf templates
        templateEngine.addDialect(new SpringSecurityDialect());
        return templateEngine;
    }

    @Bean
    public TemplateResolver templateResolver() {
        TemplateResolver templateResolver = new ServletContextTemplateResolver();
        templateResolver.setPrefix("/WEB-INF/views/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode("HTML5");
        return templateResolver;
    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
        // mapping DispatcherServlet to "/", thus overriding mapping of container's default Servlet
        // BUT static resources are still handled by container's default servlet
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/").addResourceLocations("/resources/**");
    }


    // resolver as solution for Exception handling
//    @Bean
//    public SimpleMappingExceptionResolver simpleMappingExceptionResolver() {
//        SimpleMappingExceptionResolver resolver = new SimpleMappingExceptionResolver();
//        // OR also can use method setStatusCodes(properties)
//        //resolver.addStatusCode(viewName, statusCode);
//        resolver.addStatusCode("error",404);
//        resolver.addStatusCode("error",400);
//        resolver.addStatusCode("error",401);
//        resolver.addStatusCode("error",403);
//        resolver.addStatusCode("error",500);
//        resolver.addStatusCode("error",503);
//        //set views for exception
//        Properties mapping = new Properties();
//        mapping.put("java.lang.Throwable", "error");
//        resolver.setExceptionMappings(mapping);
//        return resolver;
//    }


//    @Controller
//    static class FavIconController {
//        @RequestMapping("resources/images/favicon.ico")
//        String favIcon() {
//            return "forward:/resources/images/favicon.ico";
//        }
//    }

//    @Bean
//    public MessageSource messageSource() {
////        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
////        messageSource.setBasename("messages");
//        //reload MSG without recompiling
//        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
//        messageSource.setBasename("messages");
//        messageSource.setCacheSeconds(10);
//        return messageSource;
//    }

    //    MULTIPART
//    configuring of params in our ..WebInitializer because it doesn't have a constructor and setters
    @Bean
    public MultipartResolver multipartResolver() throws IOException {
        return new StandardServletMultipartResolver();
    }
    //alternative to StandardServletMultipartResolver - it doesn't need Servlet 3.0;
    // tmp directory is optional there (by default using tmp loc of Servlet container);
    // max multipart request size can not configuring
//    @Bean
//    public MultipartResolver multipartResolver() throws IOException {
//        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
//        multipartResolver.setUploadTempDir(new FileSystemResource("/tmp/tleaf/uploads"));
//        multipartResolver.setMaxUploadSize(2097152);
//        multipartResolver.setMaxInMemorySize(0);
//        return multipartResolver;
//    }

//    // Spring Security
//    @Override
//    public void addViewControllers(ViewControllerRegistry registry) {
//        registry.addViewController("/login").setViewName("login");
//    }


}
