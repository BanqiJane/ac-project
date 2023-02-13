package xyz.acproject.security_flux_demo.security;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import xyz.acproject.lang.exception.AuthException;

/**
 * @author Jane
 * @ClassName CustomSecurityContextRepository
 * @Description TODO
 * @date 2021/4/16 17:31
 * @Copyright:2021
 */
@Component
public class CustomSecurityContextRepository implements ServerSecurityContextRepository {
    private CustomAuthenticationManager customAuthenticationManager;
    @Override
    public Mono<Void> save(ServerWebExchange serverWebExchange, SecurityContext securityContext) {
        throw new AuthException();
    }

    @Override
    public Mono<SecurityContext> load(ServerWebExchange serverWebExchange) {
        ServerHttpRequest request = serverWebExchange.getRequest();
        String authHeader = request.getHeaders().getFirst("token");
        if(StringUtils.isNotBlank(authHeader)){
            Authentication authentication = new UsernamePasswordAuthenticationToken(authHeader,authHeader);
            return this.customAuthenticationManager.authenticate(authentication).map((a)->{
                return new SecurityContextImpl(a);
            });
        }else {
            return Mono.empty();
        }
    }

    @Autowired
    public void setCustomAuthenticationManager(CustomAuthenticationManager customAuthenticationManager) {
        this.customAuthenticationManager = customAuthenticationManager;
    }
}
