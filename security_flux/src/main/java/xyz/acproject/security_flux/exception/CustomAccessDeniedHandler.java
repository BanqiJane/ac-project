package xyz.acproject.security_flux.exception;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author Jane
 * @ClassName CustomAccessDeniedHandler
 * @Description TODO
 * @date 2021/2/19 23:11
 * @Copyright:2021
 */
@Component
public class CustomAccessDeniedHandler implements ServerAccessDeniedHandler {
    private final static Logger LOGGER = LogManager.getLogger(CustomAccessDeniedHandler.class);

    @Override
    public Mono<Void> handle(ServerWebExchange serverWebExchange, AccessDeniedException e) {
        LOGGER.debug("hande custom:{}",e.getMessage());
        serverWebExchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
        // FastJsonUtils.toJson(new Response().custom(HttpCodeEnum.tokenfail))
      //  FastJsonUtils.toJson(new Response().custom(HttpCodeEnum.tokenfail))
        return Mono.error(e);
    }
}
