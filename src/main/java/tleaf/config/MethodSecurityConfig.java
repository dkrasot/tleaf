package tleaf.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

//@Configuration
//@EnableGlobalMethodSecurity(securedEnabled = true)
public class MethodSecurityConfig {//extends GlobalMethodSecurityConfiguration {
}

//annotate methods with Spring @Secured("ROLE_USER")
// or Java @RolesAllowed("ROLE_USER") with @EnableGlobalMethodSecurity(jsr250Enabled = true)
