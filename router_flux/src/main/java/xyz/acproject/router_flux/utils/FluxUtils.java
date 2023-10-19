package xyz.acproject.router_flux.utils;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DefaultDataBuffer;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.codec.multipart.FormFieldPart;
import org.springframework.http.codec.multipart.Part;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author Jane
 * @ClassName FluxUtils
 * @Description TODO
 * @date 2022/4/29 15:29
 * @Copyright:2022
 */

public class FluxUtils {

    /**
     * @return reactor.core.publisher.Mono<java.lang.String>
     * @Description 获取一些特殊的表单（即spring处理不了的 例如带下划线(_)开头的）
     * @author Jane
     * @date 2022/4/29 15:37
     * @params [serverWebExchange, name]
     * @Copyright
     */
    public static Mono<String> getFormDataMono(ServerWebExchange serverWebExchange, String name) {
        Mono<String> mono = serverWebExchange.getFormData()
                .flatMap(formData1 -> Mono.justOrEmpty(formData1.getFirst(name))).defaultIfEmpty("");
        return mono;
    }

    public static String getFormData(ServerWebExchange serverWebExchange, String name) {
        Mono<String> mono = serverWebExchange.getFormData()
                .flatMap(formData1 -> Mono.justOrEmpty(formData1.getFirst(name))).defaultIfEmpty("");
        return mono.share().block();
    }


    public static Mono<String> getFormDataMultipartDataMono(ServerWebExchange serverWebExchange, String name) {
        return serverWebExchange.getMultipartData().flatMap(
                multipartData -> {
                    Part part = multipartData.getFirst(name);
                    if (part instanceof FormFieldPart) {
                        FormFieldPart formFieldPart = (FormFieldPart) part;
                        return Mono.just(formFieldPart.value());
                    }
                    return Mono.empty();
                }
        ).defaultIfEmpty("");
    }

    public static Flux<DataBuffer> getFormDataMultipartFileMono(ServerWebExchange serverWebExchange, String name) {
        return serverWebExchange.getMultipartData().flatMap(
                multipartData -> {
                    Part part = multipartData.getFirst(name);
                    if (part instanceof FilePart) {
                        FilePart filePart = (FilePart) part;
                        return filePart.content().collectList();
                    }
                    return Mono.empty();
                }
        ).flux().flatMap(dataBuffers -> Flux.fromIterable(dataBuffers));
    }

    public static DefaultDataBuffer getDefaultDataBuffer() {
        return new DefaultDataBufferFactory().allocateBuffer();
    }


}
