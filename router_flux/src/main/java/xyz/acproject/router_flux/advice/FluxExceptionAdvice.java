package xyz.acproject.router_flux.advice;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBufferLimitException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ServerWebInputException;
import reactor.core.publisher.Mono;
import xyz.acproject.lang.enums.HttpCodeEnum;
import xyz.acproject.lang.exception.*;
import xyz.acproject.lang.response.Response;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.nio.file.AccessDeniedException;
import java.util.Set;

/**
 * @author Jane
 * @ClassName ExceptionAdvice
 * @Description TODO
 * @date 2021/1/28 0:26
 * @Copyright:2021
 */
@RestControllerAdvice
@Order(2)
public class FluxExceptionAdvice {
    private static final Logger LOGGER = LogManager.getLogger(FluxExceptionAdvice.class);

        /**

         * 400 - Bad Request

         */
        @ResponseStatus(HttpStatus.BAD_REQUEST)
        @ExceptionHandler(ServerWebInputException.class)
        public Mono<Response>  handleMissingServletRequestParameterException(ServerWebInputException e,@RequestParam(required = false) BindingResult bindingResult) {
            String detail_error = "";
            if(bindingResult!=null){
                if(bindingResult.hasErrors()){
                    detail_error = bindingResult.getFieldError().getDefaultMessage();
                }
            }
            LOGGER.error("参数格式传递异常:{},{}",detail_error,e.getMessage());
            HttpCodeEnum httpCodeEnum = HttpCodeEnum.paramserror;
            httpCodeEnum.setCn_msg("参数格式传递异常："+detail_error);
            httpCodeEnum.setMsg(e.getMessage());
            return Mono.justOrEmpty(new Response().custom(httpCodeEnum));
        }



    /**
     * 400 - Bad Request
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Mono<Response> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        LOGGER.error("参数格式传递异常:{}", e.getMessage());
        HttpCodeEnum httpCodeEnum = HttpCodeEnum.paramserror;
        httpCodeEnum.setCn_msg("参数格式传递异常");
        httpCodeEnum.setMsg(e.getMessage());
        return Mono.justOrEmpty(new Response().custom(httpCodeEnum));
    }

    /**
     * 400 - Bad Request
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Mono<Response> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        LOGGER.error("参数验证失败", e.getMessage());
        BindingResult result = e.getBindingResult();
        FieldError error = result.getFieldError();
        String field = error.getField();
        String code = error.getDefaultMessage();
        String message = String.format("%s:%s", field, code);
        HttpCodeEnum httpCodeEnum = HttpCodeEnum.paramserror;
        httpCodeEnum.setCn_msg("参数验证失败");
        httpCodeEnum.setMsg(message);
        return Mono.justOrEmpty(new Response().custom(httpCodeEnum));
    }


//        /**
//
//         * 415 - unsupported media type
//
//         */
//        @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
//        @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
//        public Response handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException e) {
//            LOGGER.error("不支持媒体类型:{}", e);
//            return new Response().custom(HttpCodeEnum.unsupportMediaType);
//        }

    /**
     * 400 - Bad Request
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public Mono<Response> handleBindException(BindException e) {
        LOGGER.error("参数绑定失败:{}", e.getMessage());
        BindingResult result = e.getBindingResult();
        FieldError error = result.getFieldError();
        String field = error.getField();
        String code = error.getDefaultMessage();
        String message = String.format("%s:%s", field, code);
        HttpCodeEnum httpCodeEnum = HttpCodeEnum.paramserror;
        httpCodeEnum.setCn_msg("参数绑定失败");
        httpCodeEnum.setMsg(message);
        return Mono.justOrEmpty(new Response().custom(httpCodeEnum));
    }

    /**
     * 400 - Bad Request
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public Mono<Response> handleServiceException(ConstraintViolationException e) {
        LOGGER.error("参数验证失败:{}", e.getMessage());
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        ConstraintViolation<?> violation = violations.iterator().next();
        String message = violation.getMessage();
        HttpCodeEnum httpCodeEnum = HttpCodeEnum.paramserror;
        httpCodeEnum.setCn_msg("参数检验错误");
        httpCodeEnum.setMsg(message);
        return Mono.justOrEmpty(new Response().custom(httpCodeEnum));
    }

    /**
     * 400 - Bad Request
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(javax.xml.bind.ValidationException.class)
    public Mono<Response> handleValidationException(javax.xml.bind.ValidationException e) {
        LOGGER.error("xml参数验证失败:{}", e.getMessage());
        HttpCodeEnum httpCodeEnum = HttpCodeEnum.paramserror;
        httpCodeEnum.setCn_msg("参数检验错误");
        httpCodeEnum.setMsg(e.getMessage());
        return Mono.justOrEmpty(new Response().custom(httpCodeEnum));
    }


    /**
     * 400 - Bad Request
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ValidationException.class)
    public Mono<Response> handleValidationException(ValidationException e) {
        LOGGER.error("参数验证失败:{}", e.getMessage());
        HttpCodeEnum httpCodeEnum = HttpCodeEnum.paramserror;
        httpCodeEnum.setCn_msg("参数检验错误");
        httpCodeEnum.setMsg(e.getMessage());
        return Mono.justOrEmpty(new Response().custom(httpCodeEnum));
    }

//    /**
//
//     * 405 - Method Not Allowed 黑名单
//
//     */
//    @ResponseStatus(HttpStatus.UNAUTHORIZED)
//    @ExceptionHandler(WhileIPException.class)
//    public Response handleWhiteIPException(WhileIPException e) {
//        logger.error("不支持当前请求方法:{}", e);
//        return new Response().failure("while_ip_not_validate");
//    }


//        /**
//
//         * 405 - Method Not Allowed
//
//         */
//        @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
//        @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
//        public Response handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
//            LOGGER.error("不支持当前请求方法:{}", e);
//            return new Response().failure("request_method_not_supported");
//        }

    /**
     * 500 - Internal Server Error
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(ServiceException.class)
    public Mono<Response> handleServiceException(ServiceException e) {
        LOGGER.error("服务运行异常:{}", e.getMessage());
        return Mono.justOrEmpty(new Response().custom(HttpCodeEnum.syserror.cn_msg(e.getMessage())));
    }

    /**
     * 200 - Data Error
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(DataException.class)
    public Mono<Response> handleDataException(DataException e) {
        LOGGER.error("自定义错误:{}", e.getMessage());
        return Mono.justOrEmpty(new Response().failure(e.getMessage()));
    }

    /**
     * 403 - Auth Error
     */
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AuthException.class)
    public Mono<Response> handleAuthException(AuthException e) {
        LOGGER.error("权限异常:{}", e.getMessage());
        return Mono.justOrEmpty(new Response().custom(HttpCodeEnum.tokenfail));
    }


    /**
     * 403 - Auth forbidden
     */
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AccessDeniedException.class)
    public Mono<Response> handleAuthException(AccessDeniedException e) {
        LOGGER.error("权限异常:{}", e.getMessage());
        return Mono.justOrEmpty(new Response().custom(HttpCodeEnum.systemforbidden));
    }


    /**
     * 403 - Auth forbidden
     */
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(SystemForbiddenException.class)
    public Mono<Response> handleSystemForbiddenException(SystemForbiddenException e) {
        LOGGER.error("权限异常:{}", e.getMessage());
        return Mono.justOrEmpty(new Response().custom(HttpCodeEnum.systemforbidden));
    }

    /**
     * 500 - Internal Server Error
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public Mono<Response> handleException(Exception e) {
        LOGGER.error("未知错误:{}", e.getMessage());
        e.printStackTrace();
//            if(e instanceof org.springframework.web.servlet.NoHandlerFoundException){
//                LOGGER.error("404", e);
//                return new Response().custom(HttpCodeEnum.notfound);
//            }
        return Mono.justOrEmpty(new Response().custom(HttpCodeEnum.custom.msg(e.getMessage()).code(-400)));
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(CustomException.class)
    public Mono<Response> handleCustomException(CustomException e) {
        LOGGER.error("自定义异常:{}", e.getMessage());
        return Mono.justOrEmpty(new Response().custom(e.getCodeEnum()));
    }


    @ResponseStatus(HttpStatus.REQUEST_TIMEOUT)
    @ExceptionHandler(TimeoutException.class)
    public Mono<Response> handleCustomException(TimeoutException e) {
        LOGGER.error("超时异常:{}", e.getMessage());
        return Mono.justOrEmpty(new Response().custom(HttpCodeEnum.request_timeout));
    }

    @ResponseStatus(HttpStatus.PAYLOAD_TOO_LARGE)
    @ExceptionHandler(DataBufferLimitException.class)
    public Mono<Response> handleCustomException(DataBufferLimitException e) {
        LOGGER.error("请求体超出限制:{}", e.getMessage());
        e.printStackTrace();
        return Mono.justOrEmpty(new Response().custom(HttpCodeEnum.payload_too_large));
    }
//    /**
//     JwtException
//     * 203 - Auth Error
//
//     */
//    @ResponseStatus(HttpStatus.NON_AUTHORITATIVE_INFORMATION)
//    @ExceptionHandler(JwtException.class)
//    public Response HandleJwtException(JwtException e) {
//        LOGGER.error("token错误或过期:{}", e);
//        return new Response().custom(HttpCodeEnum.tokenfail);
//    }
//

//    @ExceptionHandler(RequestRejectedException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public Response handleRequestRejectedException(HttpServletRequest request, RequestRejectedException ex){
//        LOGGER.error("url安全错误:{}", ex);
//        return new Response().failure("url安全错误");
//    }
}
