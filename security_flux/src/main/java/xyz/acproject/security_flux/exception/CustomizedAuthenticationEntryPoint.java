package xyz.acproject.security_flux.exception;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import xyz.acproject.lang.enums.HttpCodeEnum;
import xyz.acproject.lang.response.Response;
import xyz.acproject.utils.FastJsonUtils;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Jane
 * @ClassName CustomizedAuthenticationEntryPoint
 * @Description TODO
 * @date 2021/2/19 23:11
 * @Copyright:2021
 */
@Component
public class CustomizedAuthenticationEntryPoint implements ServerAuthenticationEntryPoint {
    private final static Logger LOGGER = LogManager.getLogger(CustomizedAuthenticationEntryPoint.class);

    @Override
    public Mono<Void> commence(ServerWebExchange serverWebExchange, AuthenticationException e) {
        LOGGER.debug("handle auth:{}",e.getMessage());
//        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
//        response.setContentType("application/json;charset=UTF-8");
        serverWebExchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
       // FastJsonUtils.toJson(new Response().custom(HttpCodeEnum.tokenfail))
        return Mono.error(e);
    }
}
