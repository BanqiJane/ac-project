package xyz.acproject.router.advice;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import xyz.acproject.lang.enums.HttpCodeEnum;
import xyz.acproject.lang.exception.*;
import xyz.acproject.lang.response.Response;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.xml.bind.ValidationException;
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
public class ExceptionAdvice {
    private static final Logger LOGGER = LogManager.getLogger(ExceptionAdvice.class);

    /**
     * 400 - Bad Request
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public Response handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        LOGGER.error("缺少请求参数:{}", e.getMessage());
        HttpCodeEnum httpCodeEnum = HttpCodeEnum.paramserror;
        httpCodeEnum.setCn_msg("参数格式传递异常");
        httpCodeEnum.setMsg("required parameter is not present");
        return new Response().custom(httpCodeEnum);
    }

    /**
     * 400 - Bad Request
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler( MethodArgumentTypeMismatchException.class)
    public Response handleMethodArgumentTypeMismatchException( MethodArgumentTypeMismatchException e) {
        LOGGER.error("缺少请求参数:{}", e.getMessage());
        HttpCodeEnum httpCodeEnum = HttpCodeEnum.paramserror;
        httpCodeEnum.setCn_msg("参数格式传递异常");
        httpCodeEnum.setMsg("required parameter is not present");
        return new Response().custom(httpCodeEnum);
    }
    /**
     * 400 - Bad Request
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Response handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        LOGGER.error("参数格式传递异常:{}", e.getMessage());
        HttpCodeEnum httpCodeEnum = HttpCodeEnum.paramserror;
        httpCodeEnum.setCn_msg("参数格式传递异常");
        httpCodeEnum.setMsg(e.getMessage());
        return new Response().custom(httpCodeEnum);
    }

    /**
     * 400 - Bad Request
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Response handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        LOGGER.error("参数验证失败:{}", e.getMessage());
        BindingResult result = e.getBindingResult();
        FieldError error = result.getFieldError();
        String field = error.getField();
        String code = error.getDefaultMessage();
        String message = String.format("%s:%s", field, code);
        HttpCodeEnum httpCodeEnum = HttpCodeEnum.paramserror;
        httpCodeEnum.setCn_msg("参数验证失败");
        httpCodeEnum.setMsg(message);
        return new Response().custom(httpCodeEnum);
    }


    /**
     * 415 - unsupported media type
     */
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public Response handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException e) {
        LOGGER.error("不支持媒体类型:{}", e.getMessage());
        return new Response().custom(HttpCodeEnum.unsupportMediaType);
    }

    /**
     * 400 - Bad Request
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public Response handleBindException(BindException e) {
        LOGGER.error("参数绑定失败:{}", e.getMessage());
        BindingResult result = e.getBindingResult();
        FieldError error = result.getFieldError();
        String field = error.getField();
        String code = error.getDefaultMessage();
        String message = String.format("%s:%s", field, code);
        HttpCodeEnum httpCodeEnum = HttpCodeEnum.paramserror;
        httpCodeEnum.setCn_msg("参数绑定失败");
        httpCodeEnum.setMsg(message);
        return new Response().custom(httpCodeEnum);
    }

    /**
     * 400 - Bad Request
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public Response handleServiceException(ConstraintViolationException e) {
        LOGGER.error("参数验证失败:{}", e.getMessage());
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        ConstraintViolation<?> violation = violations.iterator().next();
        String message = violation.getMessage();
        HttpCodeEnum httpCodeEnum = HttpCodeEnum.paramserror;
        httpCodeEnum.setCn_msg("参数检验错误");
        httpCodeEnum.setMsg(message);
        return new Response().custom(httpCodeEnum);
    }

    /**
     * 400 - Bad Request
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ValidationException.class)
    public Response handleValidationException(ValidationException e) {
        LOGGER.error("xml参数验证失败:{}", e.getMessage());
        HttpCodeEnum httpCodeEnum = HttpCodeEnum.paramserror;
        httpCodeEnum.setCn_msg("参数检验错误");
        httpCodeEnum.setMsg(e.getMessage());
        return new Response().custom(httpCodeEnum);
    }

//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(ValidationException.class)
//    public Response handleValidationException(ValidationException e) {
//        LOGGER.error("参数验证失败:{}", e.getMessage());
//        HttpCodeEnum httpCodeEnum = HttpCodeEnum.paramserror;
//        httpCodeEnum.setCn_msg("参数检验错误");
//        httpCodeEnum.setMsg(e.getMessage());
//        return new Response().custom(httpCodeEnum);
//    }

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


    /**
     * 405 - Method Not Allowed
     */
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Response handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        LOGGER.error("不支持当前请求方法:{}", e.getMessage());
        return new Response().custom(HttpCodeEnum.requestmethoderror);
    }

    /**
     * 500 - Internal Server Error
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(ServiceException.class)
    public Response handleServiceException(ServiceException e) {
        LOGGER.error("服务运行异常:{}", e.getMessage());
        return new Response().custom(HttpCodeEnum.syserror.cn_msg(e.getMessage()));
    }

    /**
     * 200 - Data Error
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(DataException.class)
    public Response handleDataException(DataException e) {
        LOGGER.error("自定义错误:{}", e.getMessage());
        return new Response().failure(e.getMessage());
    }

    /**
     * 203 - Auth Error
     */
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AuthException.class)
    public Response handleAuthException(AuthException e) {
        LOGGER.error("权限异常:{}", e.getMessage());
        return new Response().custom(HttpCodeEnum.tokenfail);
    }


    /**
     * 403 - Auth forbidden
     */
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AccessDeniedException.class)
    public Response handleAuthException(AccessDeniedException e) {
        LOGGER.error("权限异常:{}", e.getMessage());
        return new Response().custom(HttpCodeEnum.systemforbidden);
    }


    /**
     * 403 - Auth forbidden
     */
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(SystemForbiddenException.class)
    public Response handleSystemForbiddenException(SystemForbiddenException e) {
        LOGGER.error("权限异常:{}", e.getMessage());
        return new Response().custom(HttpCodeEnum.systemforbidden);
    }

    /**
     * 404 - not found
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoHandlerFoundException.class)
    public Response NoHandlerFoundException(NoHandlerFoundException e) {
        LOGGER.error("404:{}", e.getMessage());
        return new Response().add("request_url:{}", e.getRequestURL()).custom(HttpCodeEnum.notfound);
    }

    /**
     * 500 - Internal Server Error
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public Response handleException(Exception e) {
        LOGGER.error("未知错误:{}", e.getMessage());
        e.printStackTrace();
//            if(e instanceof org.springframework.web.servlet.NoHandlerFoundException){
//                LOGGER.error("404:{}", e);
//                return new Response().custom(HttpCodeEnum.notfound);
//            }
        return new Response().custom(HttpCodeEnum.custom.msg(e.getMessage()).code(-400));
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(CustomException.class)
    public Response handleCustomException(CustomException e) {
        LOGGER.error("自定义异常:{}", e.getMessage());
        return new Response().custom(e.getCodeEnum());
    }



    @ResponseStatus(HttpStatus.REQUEST_TIMEOUT)
    @ExceptionHandler(TimeoutException.class)
    public Response handleCustomException(TimeoutException e) {
        LOGGER.error("自定义异常:{}", e.getMessage());
        return new Response().custom(HttpCodeEnum.request_timeout);
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


//    @ExceptionHandler(RequestRejectedException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public Response handleRequestRejectedException(HttpServletRequest request, RequestRejectedException ex){
//        LOGGER.error("url安全错误:{}", ex);
//        return new Response().failure("url安全错误");
//    }
}
