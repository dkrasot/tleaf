package tleaf.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.security.web.authentication.rememberme.InMemoryTokenRepositoryImpl;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;

@Configuration
@EnableWebSecurity
//this annotation auto-configuring SpringSecurity with these params: login-page="/login"; login-processing-url="/login"; logout-success-url="/login?logout"; logout-url="/logout"; authentication-failure-url="/login?error"; password-parameter="password"; username-parameter="username"
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    // https://spring.io/blog/2013/07/03/spring-security-java-config-preview-web-security/
//    @Autowired
//    public void configureGlobal(AuthenticationManagerBuilder auth) {
//        auth.inMemoryAuthentication().withUser("user").password("demo").roles("USER");
//    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("user").password("demo").roles("USER")
                .and()
                .withUser("admin").password("demo").roles("USER", "ADMIN")
                .and()
                .withUser("adminonly").password("demo").roles("ADMIN");
        //example with JDBC
//        auth.jdbcAuthentication().dataSource(LINK_TO_AUTOWIRED_DataSource)
//                .usersByUsernameQuery("select username, password, true from USERS where username=?")
//                .authoritiesByUsernameQuery("select username, 'ROLE_USER' from USERS where username=?")
//                .passwordEncoder(new StandardPasswordEncoder("53cr3t"));
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        // Ignore any request that starts with “/resources/”
        web.ignoring().antMatchers("/resources/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin().loginPage("/login")
                .and()
                .logout().logoutSuccessUrl("/") // .logoutUrl("/logout") ????
//.and().httpBasic().realmName("TLeaf") ????
//.and().csrf().disable() //??disabled by default??
//.and().rememberMe().tokenRepository(new InMemoryTokenRepositoryImpl()).tokenValiditySeconds(2419200).key("tleafKey")

                .and()
                .authorizeRequests()
                    .antMatchers("/").authenticated()
                    .antMatchers("/me").authenticated()
                    .antMatchers(HttpMethod.POST, "/tweets").authenticated()
                    //.antMatchers("/admin").access("isAuthenticated() and principal.username=='admin'")
                    //.antMatchers("/upload").hasRole("ADMIN")
                        //.access("hasRole('ROLE_ADMIN') and hasIpAdress('192.168.1.2')")//example with SpEL
                    .anyRequest().permitAll();
    }







    //TODO make CSRF-token support later...
// to remember: WITH CSRF LOGOUT must be with POST-request
//    <c:url var="logoutUrl" value="/logout"/>
//    <form action="${logoutUrl}" method="post">
//        <input type="submit" value="Log out" />
//        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
//    </form>

    //to configure http: .and().csrf().csrfTokenRepository(csrfTokenRepository()) //trying to add CSRF support

//    private CsrfTokenRepository csrfTokenRepository() {
//        HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
//        repository.setSessionAttributeName("_csrf");
//        return repository;
//    }
// problem with CSRF; java.lang.ClassNotFoundException: org.springframework.security.web.access.expression.WebSecurityExpressionHandler
// http://stackoverflow.com/questions/23113624/spring-security-error  - SOLUTION???
}

