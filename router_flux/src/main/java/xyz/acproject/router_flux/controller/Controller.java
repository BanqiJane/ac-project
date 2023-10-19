package xyz.acproject.router_flux.controller;

import org.reactivestreams.Publisher;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.ZeroCopyHttpOutputMessage;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;
import xyz.acproject.lang.enums.HttpCodeEnum;
import xyz.acproject.lang.page.PageBean;
import xyz.acproject.lang.response.Response;

/**
 * @author Jane
 * @ClassName Controller
 * @Description TODO
 * @date 2022/4/15 11:46
 * @Copyright:2022
 */
public abstract class Controller {


    protected static void data(String key, Object value) {
        Response.data(key, value);
    }

    protected Mono<Response> success() {
        return Mono.justOrEmpty(new Response().success());
    }

    protected <T> Mono<Response> success(T t) {
        return Mono.justOrEmpty(new Response().success()).map(response -> response.data(t));
    }


    protected <T> Mono<Response> success(Mono<T> t) {
        return Mono.justOrEmpty(new Response().success())
                .zipWith(t).flatMap(x -> x.getT2()!=null?Mono.just(x.getT1().data(x.getT2())):Mono.just(x.getT1()));
    }

    protected <T> Mono<Response> success(Flux<T> ts) {
        return Mono.justOrEmpty(new Response().success()).zipWith(ts.collectList()).flatMap(x -> Mono.just(x.getT1().data(x.getT2())));
    }

    protected Mono<Response> error(String message) {
        return Mono.justOrEmpty(new Response().failure(message));
    }


    protected Mono<Response> custom(HttpCodeEnum httpCodeEnum) {
        return Mono.justOrEmpty(new Response().custom(httpCodeEnum));
    }

    protected void page(PageBean<?> pageBean){
        Response.pageBean(pageBean);
    }

    protected Mono<Void> empty(){
        return Mono.empty();
    }

    protected Mono<Void> file(ServerWebExchange serverWebExchange, Publisher<? extends DataBuffer> body){
        return serverWebExchange.getResponse().writeWith(body);
    }

    protected Mono<Void> file(ZeroCopyHttpOutputMessage zeroCopyHttpOutputMessage, Publisher<? extends DataBuffer> body){
        return zeroCopyHttpOutputMessage.writeWith(body);
    }

    protected Mono<Void> body(ServerWebExchange serverWebExchange, DataBuffer defaultDataBuffer){
        Flux<DataBuffer> dataBufferFlux = Flux.create((FluxSink<DataBuffer> emitter) -> {
            emitter.next(defaultDataBuffer);
            emitter.complete();
        });
        return serverWebExchange.getResponse().writeWith(dataBufferFlux);
    }

}
