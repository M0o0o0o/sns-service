package mooyeol.snsservice.security.config;

import mooyeol.snsservice.security.common.AjaxLoginAuthenticationEntryPoint;
import mooyeol.snsservice.security.filter.AjaxLoginProcessingFilter;
import mooyeol.snsservice.security.handler.AjaxAccessdeniedHandler;
import mooyeol.snsservice.security.handler.AjaxAuthenticationFailurHandler;
import mooyeol.snsservice.security.handler.AjaxAuthenticationSucessHandler;
import mooyeol.snsservice.security.provider.AjaxAuthenticationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
public class AjaxSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(ajaxAuthenticationProvider());
    }

    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .antMatcher("/**")
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/post/**").authenticated()
                .antMatchers(HttpMethod.PATCH, "/post/**").authenticated()
                .antMatchers(HttpMethod.DELETE, "/post/**").authenticated()
                .antMatchers(HttpMethod.POST, "/post/like/**").authenticated()
                .anyRequest().permitAll()
                .and()
                .addFilterBefore(ajaxLoginProcessingFilter(), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling()
                .authenticationEntryPoint(new AjaxLoginAuthenticationEntryPoint())
                .accessDeniedHandler(ajaxAccessdeniedHandler())
        ;

        http.csrf().disable();
    }

    protected AjaxLoginProcessingFilter ajaxLoginProcessingFilter() throws Exception {
        AjaxLoginProcessingFilter filter = new AjaxLoginProcessingFilter();
        filter.setAuthenticationManager(authenticationManagerBean());
        filter.setAuthenticationSuccessHandler(ajaxAuthenticationSucessHandler());
        filter.setAuthenticationFailureHandler(ajaxAuthenticationFailurHandler());
        return filter;
    }

    @Bean
    public AjaxAccessdeniedHandler ajaxAccessdeniedHandler() {
        return new AjaxAccessdeniedHandler();
    }
    @Bean
    public AuthenticationProvider ajaxAuthenticationProvider() {
        return new AjaxAuthenticationProvider(passwordEncoder());
    }

    @Bean
    public AjaxAuthenticationSucessHandler ajaxAuthenticationSucessHandler() {
        return new AjaxAuthenticationSucessHandler();
    }

    @Bean
    public AjaxAuthenticationFailurHandler ajaxAuthenticationFailurHandler() {
        return new AjaxAuthenticationFailurHandler();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}
