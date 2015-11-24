package tleaf.config;

//DISABLING TEMPORARY

import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

public class SecurityWebInitializer extends AbstractSecurityWebApplicationInitializer {
    //springSecurityFilterChain is mapped to “/*”
    //springSecurityFilterChain uses the dispatch types of ERROR and REQUEST
    //The springSecurityFilterChain mapping is inserted before any servlet Filter mappings that have already been configured

/////like this XML-configuration:
//    <filter>
//        <filter-name>springSecurityFilterChain</filter-name>
//        <filter-class>org.springframework.web.filter.DelegatingFilterProxy</filter-class>
//    </filter>
//
//    <filter-mapping>
//        <filter-name>springSecurityFilterChain</filter-name>
//        <url-pattern>/*</url-pattern>
//        <dispatcher>ERROR</dispatcher>
//        <dispatcher>REQUEST</dispatcher>
//    </filter-mapping>
}