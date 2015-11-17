package tleaf.config;

//DISABLING TEMPORARY

//@Configuration
//@EnableGlobalMethodSecurity(securedEnabled = true)
public class MethodSecurityConfig {//extends GlobalMethodSecurityConfiguration {
}

//annotate methods with Spring @Secured("ROLE_USER")
// or Java @RolesAllowed("ROLE_USER") with @EnableGlobalMethodSecurity(jsr250Enabled = true)
