package xyz.acproject.router_flux.controller;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ZeroCopyHttpOutputMessage;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

/**
 * @author Jane
 * @ClassName FController
 * @Description TODO
 * @date 2022/7/1 15:15
 * @Copyright:2022
 */

public abstract class FController extends Controller {

    protected Mono<Void> file(ServerWebExchange serverWebExchange,String fileName, DataBuffer defaultDataBuffer){
        ZeroCopyHttpOutputMessage zeroCopyHttpOutputMessage = (ZeroCopyHttpOutputMessage) serverWebExchange.getResponse();
        HttpHeaders headers = zeroCopyHttpOutputMessage.getHeaders();
        try {
            headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + new String(fileName.getBytes("utf-8"), "iso-8859-1") + ".xlsx");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //vnd.ms-excel 可以替换为 octet-stream
        MediaType application = new MediaType("application", "octet-stream", Charset.forName("UTF-8"));
        headers.setContentType(application);

        Flux<DataBuffer> dataBufferFlux = Flux.create((FluxSink<DataBuffer> emitter) -> {
            emitter.next(defaultDataBuffer);
            emitter.complete();
        });
        return zeroCopyHttpOutputMessage.writeWith(dataBufferFlux);
    }

}
