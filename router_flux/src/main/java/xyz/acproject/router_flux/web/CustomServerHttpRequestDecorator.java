package xyz.acproject.router_flux.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qiniu.util.IOUtils;
import io.netty.buffer.UnpooledByteBufAllocator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.NettyDataBufferFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import static reactor.core.scheduler.Schedulers.single;

/**
 * @author Admin
 * @ClassName CustomServerHttpRequestDecorator
 * @Description TODO
 * @date 2022/12/16 14:36
 * @Copyright:2022
 */
public class CustomServerHttpRequestDecorator extends ServerHttpRequestDecorator {

    private volatile Logger LOGGER = LogManager.getLogger(CustomServerHttpRequestDecorator.class);
    private final StringWriter cachedCopy = new StringWriter();
    private InputStream inputStream;
    private DataBuffer bodyDataBuffer;
    private int getBufferTime = 0;


    private byte[] bytes;


    protected CustomServerHttpRequestDecorator(ServerHttpRequest delegate) {
        super(delegate);
    }

    @Override
    public Flux<DataBuffer> getBody() {
        if (getBufferTime == 0) {
            getBufferTime++;
            Flux<DataBuffer> flux = super.getBody();
            return flux
                    .publishOn(single())
                    .map(this::cache)
                    .doOnComplete(() -> trace(getDelegate(), cachedCopy.toString()));


        } else {
            return Flux.just(getBodyMore());
        }

    }


    private DataBuffer getBodyMore() {
        NettyDataBufferFactory nettyDataBufferFactory = new NettyDataBufferFactory(new UnpooledByteBufAllocator(false));
        DataBuffer dataBuffer = nettyDataBufferFactory.wrap(bytes);
        bodyDataBuffer = dataBuffer;
        return bodyDataBuffer;
    }

    private DataBuffer cache(DataBuffer buffer) {
        try {
            inputStream = buffer.asInputStream();
            bytes = IOUtils.toByteArray(inputStream);
            NettyDataBufferFactory nettyDataBufferFactory = new NettyDataBufferFactory(new UnpooledByteBufAllocator(false));
            DataBuffer dataBuffer = nettyDataBufferFactory.wrap(bytes);
            bodyDataBuffer = dataBuffer;
            return bodyDataBuffer;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void trace(ServerHttpRequest request, String requestBody) {
        LOGGER.trace(requestBody);
    }

}
