package xyz.acproject.router_flux.advice;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.*;
import org.springframework.web.server.MethodNotAllowedException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.UnsupportedMediaTypeStatusException;
import reactor.core.publisher.Mono;
import xyz.acproject.lang.enums.HttpCodeEnum;
import xyz.acproject.lang.response.Response;

/**
 * @author Jane
 * @ClassName FluxGlobalErrConfig
 * @Description TODO
 * @date 2021/3/28 10:08
 * @Copyright:2021
 */
public class GlobalErrorWebExceptionHandler extends AbstractErrorWebExceptionHandler {

    private final static Logger LOGGER = LogManager.getLogger(GlobalErrorWebExceptionHandler.class);
//
//    public GlobalErrorWebExceptionHandler(ErrorAttributes errorAttributes, ResourceProperties resourceProperties, ApplicationContext applicationContext) {
//        super(errorAttributes, resourceProperties, applicationContext);
//    }

    public GlobalErrorWebExceptionHandler(ErrorAttributes errorAttributes, WebProperties.Resources webProperties, ApplicationContext applicationContext) {
        super(errorAttributes, webProperties, applicationContext);
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), request -> this.renderErrorResponse(request, errorAttributes.getError(request)));
    }

    private Mono<ServerResponse> renderErrorResponse(ServerRequest request, Throwable e) {
        //输出异常堆栈信息
//        e.printStackTrace();
        LOGGER.error("总线异常：{}",e.getMessage());
        if (e instanceof ResponseStatusException) {
            //404
            if (HttpStatus.NOT_FOUND.equals(((ResponseStatusException) e).getStatus()))
                return ServerResponse.status(HttpStatus.NOT_FOUND)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(new Response().add("url", request.exchange().getRequest().getURI().toString()).custom(HttpCodeEnum.notfound)));
                // method no support
            else if (e instanceof MethodNotAllowedException)
                return ServerResponse.status(HttpStatus.METHOD_NOT_ALLOWED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(new Response().custom(HttpCodeEnum.requestmethoderror)));
                //media type no support  2020.04.28 remove MediaTypeNotSupportedStatusException
            else if (e instanceof UnsupportedMediaTypeStatusException || HttpStatus.UNSUPPORTED_MEDIA_TYPE.equals(((ResponseStatusException) e).getStatus()))
                return ServerResponse.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(new Response().custom(HttpCodeEnum.unsupportMediaType)));
            else
                return ServerResponse.status(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(new Response().custom(HttpCodeEnum.custom.msg(e.getMessage()).code(-400))));
//        else if(e instanceof ServerWebInputException)
//                return ServerResponse.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .body(BodyInserters.fromValue(new Response().custom(HttpCodeEnum.unsupportMediaType)));
//            else
//                e.printStackTrace();
        }
        //为了避免 网络供应商、dns解析商 拦截  此处全部返回200    后续上https之后这里可以正常返回
        return ServerResponse.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(new Response().custom(HttpCodeEnum.custom.msg(e.getMessage()).code(-400))));
    }

}