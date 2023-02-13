package xyz.acproject.security_demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.RegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import xyz.acproject.security.exception.CustomAccessDeniedHandler;
import xyz.acproject.security.exception.CustomizedAuthenticationEntryPoint;
import xyz.acproject.security_demo.handler.CustomLoginFailureHandler;
import xyz.acproject.security_demo.handler.CustomLoginSuccessHandler;
import xyz.acproject.security_demo.security.*;

import javax.annotation.Resource;

/**
 * @author Jane
 * @ClassName SecurityTestConfig
 * @Description TODO
 * @date 2021/3/29 11:02
 * @Copyright:2021
 */
@Configuration
@Import(SecurityTestConfig.RolePrefixConfiguration.class)
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, jsr250Enabled = true, securedEnabled = true)
public class SecurityTestConfig extends WebSecurityConfigurerAdapter {

    @Resource
    private CustomAccessDeniedHandler customAccessDeniedHandler;
    @Resource
    private CustomizedAuthenticationEntryPoint customizedAuthenticationEntryPoint;
    private UserDetailServiceImpl userDetailService;
    private WxUserDetailServiceImpl wxUserDetailService;

    public static class RolePrefixConfiguration {
        @Bean
        GrantedAuthorityDefaults grantedAuthorityDefaults() {
            // 去除 ROLE_ 前缀
            return new GrantedAuthorityDefaults("");
        }
    }
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(customAuthenticationProvider()).userDetailsService(userDetailService)
                .passwordEncoder(NoOpPasswordEncoder.getInstance());
        auth.authenticationProvider(wxAuthenticationProvider()).userDetailsService(wxUserDetailService)
        .passwordEncoder(NoOpPasswordEncoder.getInstance());
//        super.configure(auth);
    }

    @Bean
    CustomAuthenticationProvider customAuthenticationProvider(){
        return new CustomAuthenticationProvider();
    }

    @Bean
    WxAuthenticationProvider wxAuthenticationProvider(){
        return new WxAuthenticationProvider();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .authorizeRequests()
//                .antMatchers("/security/**").permitAll()
//                .antMatchers("/api/**").permitAll()
                .antMatchers("/test/**").authenticated()
                .anyRequest().permitAll()
//                .anyRequest().authenticated()
//                .and().apply(securityConfigurerAdapter())
               // .and().formLogin().successHandler(new CustomLoginSuccessHandler()).failureHandler(new CustomLoginFailureHandler()).loginProcessingUrl("/admin/login").permitAll()
                .and().headers().frameOptions().disable()
                .and().exceptionHandling().accessDeniedHandler(customAccessDeniedHandler)
                .and().exceptionHandling().authenticationEntryPoint(customizedAuthenticationEntryPoint);
//        http.addFilterBefore(adminSecurityFilter, UsernamePasswordAuthenticationFilter.class);
//        super.configure(http);
//        http.sessionManagement().maximumSessions(1);
        http.rememberMe().rememberMeServices(adminTokenBasedRememberMeServices()).userDetailsService(userDetailService).key
                ("testc");
        http.addFilterAt(customAuthenticationFilter(),UsernamePasswordAuthenticationFilter.class);
        http.addFilterAt(wxAuthenticationFilter(),UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
    @Bean
    public RememberMeServices adminTokenBasedRememberMeServices(){
        TokenBasedRememberMeServices tokenBasedRememberMeServices = new TokenBasedRememberMeServices("testc",userDetailService);
        tokenBasedRememberMeServices.setTokenValiditySeconds(36000);
        return tokenBasedRememberMeServices;
    }

    @Bean
    CustomAuthenticationFilter customAuthenticationFilter() throws Exception {
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter();
        customAuthenticationFilter.setAuthenticationSuccessHandler(new CustomLoginSuccessHandler());
        customAuthenticationFilter.setAuthenticationFailureHandler(new CustomLoginFailureHandler());
        customAuthenticationFilter.setRememberMeServices(adminTokenBasedRememberMeServices());
        customAuthenticationFilter.setFilterProcessesUrl("/admin/login");
        customAuthenticationFilter.setAuthenticationManager(authenticationManagerBean());
        return customAuthenticationFilter;
    }
    @Bean
    WxAuthenticationFilter wxAuthenticationFilter() throws Exception {
        WxAuthenticationFilter wxAuthenticationFilter = new WxAuthenticationFilter();
//        wxAuthenticationFilter.setAuthenticationManager(wxAuthenticationManager);
        wxAuthenticationFilter.setAuthenticationSuccessHandler(new CustomLoginSuccessHandler());
        wxAuthenticationFilter.setAuthenticationFailureHandler(new CustomLoginFailureHandler());
        wxAuthenticationFilter.setFilterProcessesUrl("/admin/token");
        wxAuthenticationFilter.setAuthenticationManager(authenticationManagerBean());
        return wxAuthenticationFilter;
    }
    @Bean
    public RegistrationBean securityFilterRegistration() throws Exception {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean(wxAuthenticationFilter());
//            registrationBean.addUrlPatterns(
//                    "/api/post/**",
//                    ""
//            );
        registrationBean.setEnabled(false);
        return registrationBean;
    }
    @Autowired
    public void setUserDetailService(UserDetailServiceImpl userDetailService) {
        this.userDetailService = userDetailService;
    }
    @Autowired
    public void setWxUserDetailService(WxUserDetailServiceImpl wxUserDetailService) {
        this.wxUserDetailService = wxUserDetailService;
    }


}
