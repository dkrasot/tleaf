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

        http.formLogin()
                .loginPage("/login")
                //.loginProcessingUrl("/j_spring_security_check")
//                .defaultSuccessUrl("/index")
                .defaultSuccessUrl("/")
                .failureUrl("/login-error")
                .usernameParameter("username")
                .passwordParameter("password")
                .permitAll();

//        http.logout()
//                //.logoutUrl("/logout") //default?
//                .logoutSuccessUrl("/")
//                .permitAll();
                //.invalidateHttpSession(true);  //??? cancelling Session
        http.logout()
                .permitAll()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                        //.logoutSuccessUrl("/login?logout")
                .invalidateHttpSession(true);

        //org.springframework.security.authentication.AnonymousAuthenticationToken
        //'principal.authorities' with anonymous user
        //TODO FIX THIS page : .antMatchers("/").authenticated() for ANONYMOUS user

        http.authorizeRequests()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/user/**").hasRole("USER")
                .antMatchers("/shared/**").hasAnyRole("USER", "ADMIN")


                //.antMatchers("/").authenticated()
                .antMatchers("/").permitAll()


                //.antMatchers("/profile/**").permitAll()
                .antMatchers("/me").authenticated() //todo create PROFILE page "/me" of logged user
                .antMatchers(HttpMethod.POST, "/tweets").authenticated()
                //.antMatchers("/upload").hasRole("ADMIN")
                .anyRequest().permitAll();//.authenticated();

                //.antMatchers("/admin").access("isAuthenticated() and principal.username=='admin'")
                //.access("hasRole('ROLE_ADMIN') and hasIpAdress('192.168.1.2')")//example with SpEL

//.and().httpBasic().realmName("TLeaf") ????
//.and().rememberMe().tokenRepository(new InMemoryTokenRepositoryImpl()).tokenValiditySeconds(2419200).key("tleafKey")

//VERSION 25/11/2015

//        http.logout()
//                .logoutSuccessUrl("/")
//                .permitAll();
//        http.authorizeRequests()
//                .antMatchers("/admin/**").hasRole("ADMIN")
//                .antMatchers("/user/**").hasRole("USER")
//                .antMatchers("/shared/**").hasAnyRole("USER","ADMIN")
//
//                .antMatchers("/").authenticated()
//                .antMatchers("/me").authenticated()
//                //.antMatchers(HttpMethod.POST, "/tweets").authenticated()
//                .anyRequest().permitAll();
//        http.logout()
//                .permitAll()
//                .logoutUrl("/logout")
//                .logoutSuccessUrl("/login?logout")
//                .invalidateHttpSession(true);


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

