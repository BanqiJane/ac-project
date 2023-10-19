package xyz.acproject.security_demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import xyz.acproject.security.exception.CustomAccessDeniedHandler;
import xyz.acproject.security.exception.CustomizedAuthenticationEntryPoint;
import xyz.acproject.security_demo.security.CustomAuthenticationManager;
import xyz.acproject.security_demo.security.CustomFilter;
import xyz.acproject.security_demo.security.CustomSecurityContextRepository;
import xyz.acproject.security_demo.security.StudentSecurityContextRepository;

import javax.annotation.Resource;


/**
 * @author Jane
 * @ClassName SecurityTestConfig
 * @Description TODO
 * @date 2021/3/29 11:02
 * @Copyright:2021
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, jsr250Enabled = true, securedEnabled = true)
public class SecurityTestConfig {


    @Resource
    private CustomAccessDeniedHandler customAccessDeniedHandler;

    @Resource
    private CustomizedAuthenticationEntryPoint customizedAuthenticationEntryPoint;


    @Resource
    private CustomAuthenticationManager customAuthenticationManager;

    @Resource
    private CustomSecurityContextRepository customSecurityContextRepository;


    @Resource
    private StudentSecurityContextRepository studentSecurityContextRepository;

    @Resource
    private CustomFilter customFilter;

    @Bean
    @Order(0)
    public SecurityFilterChain studentSecurityFilterChain(HttpSecurity http) throws Exception {
        //  .antMatcher() 指定路由
        http.antMatcher("/student/**")
                .exceptionHandling()
                .authenticationEntryPoint(customizedAuthenticationEntryPoint)
                .accessDeniedHandler(customAccessDeniedHandler)
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin().disable()
                .httpBasic().disable()
                .securityContext()
                .securityContextRepository(studentSecurityContextRepository)
                .and()
//                .authenticationManager(customAuthenticationManager)
                .authorizeHttpRequests()
                .antMatchers(HttpMethod.OPTIONS).permitAll()
                .antMatchers("/student/auth/**").permitAll()
                .anyRequest().permitAll()
                .and()
//                .addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class)
                .headers().cacheControl();
        return http.build();
    }

    @Bean
    @Order(1)
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
      //  .antMatcher() 指定路由
        http
                .exceptionHandling()
                .authenticationEntryPoint(customizedAuthenticationEntryPoint)
                .accessDeniedHandler(customAccessDeniedHandler)
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin().disable()
                .httpBasic().disable()
                .securityContext()
                .securityContextRepository(customSecurityContextRepository)
                .and()
                .authenticationManager(customAuthenticationManager)
                .authorizeHttpRequests()
                .antMatchers(HttpMethod.OPTIONS).permitAll()
//                .antMatchers("/test/**").permitAll()
                .antMatchers("/login/**").permitAll()
                .antMatchers("/manager/permission/test2").permitAll()
                .anyRequest().permitAll()
                .and()
//                .addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class)
                .headers().cacheControl();
        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //去除ROLE_前缀
    @Bean
    GrantedAuthorityDefaults grantedAuthorityDefaults() {
        return new GrantedAuthorityDefaults("");
    }

}
