package xyz.acproject.security_flux.config;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.result.view.ViewResolver;
import xyz.acproject.router_flux.advice.GlobalErrorWebExceptionHandler;
import xyz.acproject.security_flux.advice.SecurityErrorWebExceptionHandler;

import java.util.stream.Collectors;

/**
 * @author Jane
 * @ClassName FluxSecurityConfig
 * @Description TODO
 * @date 2021/4/16 23:53
 * @Copyright:2021
 */
@Configuration
@EnableWebFlux
public class FluxSecurityConfig {
    @Bean
    @Primary
    @Order(-2)
    public ErrorWebExceptionHandler errorWebExceptionHandlerSecurity(ErrorAttributes errorAttributes,
                                                                     WebProperties.Resources webProperties, ObjectProvider<ViewResolver> viewResolvers,
                                                                     ServerCodecConfigurer serverCodecConfigurer, ApplicationContext applicationContext) {
        SecurityErrorWebExceptionHandler exceptionHandler = new SecurityErrorWebExceptionHandler(errorAttributes,
                webProperties, applicationContext);
        //必须手动设置 下面三项配置 特别是messageWriters
        exceptionHandler.setViewResolvers(viewResolvers.orderedStream().collect(Collectors.toList()));
        exceptionHandler.setMessageWriters(serverCodecConfigurer.getWriters());
        exceptionHandler.setMessageReaders(serverCodecConfigurer.getReaders());
        return exceptionHandler;
    }
}
