package xyz.acproject.router_flux.web;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.ServerWebExchangeDecorator;

/**
 * @author Admin
 * @ClassName CustomServerWebExchangeDecorator
 * @Description TODO
 * @date 2022/12/16 14:41
 * @Copyright:2022
 */
public final class CustomServerWebExchangeDecorator extends ServerWebExchangeDecorator {
    private final ServerHttpRequestDecorator requestDecorator;

    public CustomServerWebExchangeDecorator(ServerWebExchange delegate) {
        super(delegate);
        this.requestDecorator = new CustomServerHttpRequestDecorator(delegate.getRequest());
    }

    @Override
    public ServerHttpRequest getRequest() {
        return requestDecorator;
    }
}
