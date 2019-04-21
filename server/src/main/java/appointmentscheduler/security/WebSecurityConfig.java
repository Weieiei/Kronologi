package appointmentscheduler.security;

import appointmentscheduler.controller.rest.GoogleCalendarController;
import appointmentscheduler.service.user.UserDetailsEmailService;
import appointmentscheduler.util.AccessDeniedHandlerApp;
import appointmentscheduler.util.GoogleApiFilter;
import appointmentscheduler.util.JwtAuthenticationEntryPoint;
import appointmentscheduler.util.JwtAuthenticationFilter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig  {



    private final static Log logger = LogFactory.getLog(WebSecurityConfig.class);

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }



    @Autowired
    private UserDetailsEmailService userDetailsEmailService;


    @Autowired
    public void globalUserDetails(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsEmailService).passwordEncoder(bCryptPasswordEncoder());
    }


    @Configuration
    @Order(1)
    public static class InternalApiSecurity extends WebSecurityConfigurerAdapter {

        @Autowired
        private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

        @Autowired
        private JwtAuthenticationFilter jwtAuthenticationFilter;

        @Bean
        @Override
        public AuthenticationManager authenticationManagerBean() throws Exception {
            return super.authenticationManagerBean();
        }



        @Override
        public void configure(WebSecurity web){
            web.ignoring().antMatchers("/external/**");
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            //antMatchers("/account/{\\d+}/download").access("hasAnyAuthority('ROLE_TOKENSAVED')")
            http.csrf().disable()
                    .cors().and()
                    .authorizeRequests()
                    .antMatchers("/api/user/login", "/api/user/register", "/api/password/reset", "/api/password/forgot").permitAll()
                    .regexMatchers("\\/api\\/user\\/verification\\?hash=.*").permitAll()
                    .antMatchers("/external/google/login/google").authenticated()
                    .antMatchers("/api/*").authenticated()
                    .and()
                    .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint)
                    .and()
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);


        }


        @Bean
        public DefaultWebSecurityExpressionHandler defaultWebSecurityExpressionHandler() {
            DefaultWebSecurityExpressionHandler defaultWebSecurityExpressionHandler = new DefaultWebSecurityExpressionHandler();
            defaultWebSecurityExpressionHandler.setDefaultRolePrefix("");
            return defaultWebSecurityExpressionHandler;
        }

        @Bean
        public FilterRegistrationBean<JwtAuthenticationFilter> jwtFilter(){
            FilterRegistrationBean<JwtAuthenticationFilter> registrationBean
                    = new FilterRegistrationBean<>();

            registrationBean.setFilter(jwtAuthenticationFilter);
            registrationBean.addUrlPatterns("/api/*");
            registrationBean.addUrlPatterns("/external/google/login/google");

            return registrationBean;
        }

    }


    @Configuration
    @Order(2)
    public static class ExternalApiSecurity extends WebSecurityConfigurerAdapter{



        @Override
        public void configure(WebSecurity web){
            web.ignoring().antMatchers("/api/**");
        }

        @Autowired
        private AccessDeniedHandlerApp accessDeniedHandlerApp;


        @Autowired
        private GoogleApiFilter apiFilter;

        @Bean
        @Override
        public AuthenticationManager authenticationManagerBean() throws Exception {
            return super.authenticationManagerBean();
        }


        @Override
        protected void configure(HttpSecurity http) throws Exception {
            //antMatchers("/account/{\\d+}/download").access("hasAnyAuthority('ROLE_TOKENSAVED')")
            http.csrf().disable()
                    .cors().and()
                    .antMatcher("external/google/login/calendarCallback").authorizeRequests()
                    .anyRequest().authenticated()
                    .and()
                    .exceptionHandling().accessDeniedHandler(accessDeniedHandlerApp)
                    .and()
                    .addFilterAfter(apiFilter, UsernamePasswordAuthenticationFilter.class);

        }


        @Bean
        public FilterRegistrationBean<GoogleApiFilter> loggingFilter(){
            FilterRegistrationBean<GoogleApiFilter> registrationBean
                    = new FilterRegistrationBean<>();

            registrationBean.setFilter(apiFilter);
            registrationBean.addUrlPatterns("/external/google/login/calendarCallback");

            return registrationBean;
        }
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {

        return new WebMvcConfigurer() {

            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedMethods("GET", "POST", "PUT", "DELETE")
                        .allowedOrigins("http://localhost:4200", "http://localhost");
            }

        };
    }
}

