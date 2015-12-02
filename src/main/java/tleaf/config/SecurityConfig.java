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
//SpringSecurity auto-config: login-page="/login"; login-processing-url="/login";
// logout-success-url="/login?logout"; logout-url="/logout"; authentication-failure-url="/login?error";
// password-parameter="password"; username-parameter="username"
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("user").password("demo").roles("USER")
                .and()
                .withUser("useradmin").password("demo").roles("USER", "ADMIN")
                .and()
                .withUser("admin").password("demo").roles("ADMIN");
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

        http.csrf().disable();

        http.authorizeRequests()
                .antMatchers("/","/profile/**").permitAll() // TODO (A) ??? 1 /profile/signUp -> /signUp or /register
                .antMatchers(HttpMethod.POST, "/tweets").authenticated()
                .antMatchers(HttpMethod.GET, "/tweets").permitAll()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/user/**").hasRole("USER")
                .antMatchers("/shared/**").hasAnyRole("USER", "ADMIN")
                //.antMatchers("/me").authenticated() //TODO HTML 1 create PROFILE page "/me" OR /{username} of logged user
                //.antMatchers("/upload").hasRole("ADMIN")
                .anyRequest().authenticated();
                //.anyRequest().permitAll();
//.antMatchers("/admin").access("isAuthenticated() and principal.username=='admin'")
//.access("hasRole('ROLE_ADMIN') and hasIpAdress('192.168.1.2')")//example with SpEL

        http.formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/")
                .failureUrl("/login-error")
                .usernameParameter("username")
                .passwordParameter("password")
                .permitAll();
        http.logout()
                .permitAll()
                .logoutUrl("/logout")
                //.logoutSuccessUrl("/")
                .invalidateHttpSession(true);

        //http.exceptionHandling().accessDeniedPage("/error"); // HANDLE only 403 NO ACCESS ..

//.and().httpBasic().realmName("TLeaf") ????
//.and().rememberMe().tokenRepository(new InMemoryTokenRepositoryImpl()).tokenValiditySeconds(2419200).key("tleafKey")

    }



    //TODO?  CSRF 0 make CSRF-token support later...
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

