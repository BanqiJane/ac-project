package xyz.acproject.security_flux.advice;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.web.firewall.RequestRejectedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;
import xyz.acproject.lang.enums.HttpCodeEnum;
import xyz.acproject.lang.response.Response;

/**
 * @author Jane
 * @ClassName SecurityAdvice
 * @Description 该类优先级要比 router的总异常处理器要高 如果新增处理器 请优先级必比总线高
 * @date 2021/2/25 0:28
 * @Copyright:2021
 */
@RestControllerAdvice
@Order(1)
public class SecurityAdvice {
        private static final Logger LOGGER = LogManager.getLogger(SecurityAdvice.class);


        @ResponseStatus(HttpStatus.FORBIDDEN)
        @ExceptionHandler(InternalAuthenticationServiceException.class)
        public Mono<Response> handleBadCredentialsException(InternalAuthenticationServiceException e) {
            LOGGER.error("账号或密码错误:{}", e.getMessage());
            return Mono.justOrEmpty(new Response().custom(HttpCodeEnum.passworderror));
        }

        /**

         * 203 - Auth Error

         */
        @ResponseStatus(HttpStatus.FORBIDDEN)
        @ExceptionHandler(BadCredentialsException.class)
        public Mono<Response> handleBadCredentialsException(BadCredentialsException e) {
            LOGGER.error("账号或密码错误:{}", e.getMessage());
            return Mono.justOrEmpty(new Response().custom(HttpCodeEnum.passworderror));
        }

    /**
     JwtException
     * 203 - Auth Error

     */
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(JwtException.class)
    public  Mono<Response> HandleJwtException(JwtException e) {
        LOGGER.error("token错误或过期:{}", e.getMessage());
        return Mono.justOrEmpty(new Response().custom(HttpCodeEnum.tokenfail));
    }


    @ExceptionHandler(RequestRejectedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Mono<Response> handleRequestRejectedException(RequestRejectedException e){
        LOGGER.error("url安全错误:{}", e.getMessage());
        return Mono.justOrEmpty(new Response().custom(HttpCodeEnum.urlerror));
    }

    @ExceptionHandler(ExpiredJwtException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Mono<Response> handleExpiredJwtException(ExpiredJwtException e){
        LOGGER.error("token过期:{}", e.getMessage());
        return Mono.justOrEmpty(new Response().custom(HttpCodeEnum.tokenfail));
    }
    @ExceptionHandler(AuthenticationCredentialsNotFoundException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Mono<Response> handleAuthenticationCredentialsNotFoundException(AuthenticationCredentialsNotFoundException e){
        LOGGER.error("token错误:{}", e.getMessage());
        return Mono.justOrEmpty(new Response().custom(HttpCodeEnum.tokenfail));
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Mono<Response>  handleExpiredJwtException(AccessDeniedException e){
        LOGGER.error("权限不足:{}", e.getMessage());
        return Mono.justOrEmpty(new Response().custom(HttpCodeEnum.systemforbidden));
    }

}
