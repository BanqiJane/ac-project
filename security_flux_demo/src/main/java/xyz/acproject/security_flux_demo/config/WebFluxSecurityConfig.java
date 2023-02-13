package xyz.acproject.security_flux_demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import xyz.acproject.security_flux.exception.CustomAccessDeniedHandler;
import xyz.acproject.security_flux.exception.CustomizedAuthenticationEntryPoint;
import xyz.acproject.security_flux_demo.security.CustomAuthenticationManager;
import xyz.acproject.security_flux_demo.security.CustomSecurityContextRepository;

import javax.annotation.Resource;

/**
 * @author Jane
 * @ClassName WebFluxSecurityConfog
 * @Description TODO
 * @date 2021/4/16 17:02
 * @Copyright:2021
 */
@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class WebFluxSecurityConfig {
    private CustomAccessDeniedHandler customAccessDeniedHandler;
    private CustomizedAuthenticationEntryPoint customizedAuthenticationEntryPoint;
    @Resource(name = "customAuthenticationManager")
    private CustomAuthenticationManager customAuthenticationManager;
    @Resource(name = "customSecurityContextRepository")
    private CustomSecurityContextRepository customSecurityContextRepository;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http){
        return http
                .exceptionHandling()
                .authenticationEntryPoint(customizedAuthenticationEntryPoint).accessDeniedHandler(customAccessDeniedHandler).and()
                .csrf().disable()
                .formLogin().disable()
                .httpBasic().disable()
                .authenticationManager(customAuthenticationManager)
                .securityContextRepository(customSecurityContextRepository)
                .authorizeExchange()
                .pathMatchers(HttpMethod.OPTIONS).permitAll()
                .pathMatchers("/manager/permission/test2").permitAll()
                .pathMatchers("/auth/**").permitAll()
                .pathMatchers("/user/**").permitAll()
                .anyExchange().authenticated()
                .and().build();
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

    @Autowired
    public void setCustomAccessDeniedHandler(CustomAccessDeniedHandler customAccessDeniedHandler) {
        this.customAccessDeniedHandler = customAccessDeniedHandler;
    }
    @Autowired
    public void setCustomizedAuthenticationEntryPoint(CustomizedAuthenticationEntryPoint customizedAuthenticationEntryPoint) {
        this.customizedAuthenticationEntryPoint = customizedAuthenticationEntryPoint;
    }
//    @Autowired
//    public void setCustomAuthenticationManager(CustomAuthenticationManager customAuthenticationManager) {
//        this.customAuthenticationManager = customAuthenticationManager;
//    }
//    @Autowired
//    public void setCustomSecurityContextRepository(CustomSecurityContextRepository customSecurityContextRepository) {
//        this.customSecurityContextRepository = customSecurityContextRepository;
//    }

}
