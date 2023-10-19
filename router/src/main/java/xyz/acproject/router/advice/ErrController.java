package xyz.acproject.router.advice;



import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.NoHandlerFoundException;
import xyz.acproject.lang.exception.SystemForbiddenException;
import xyz.acproject.utils.io.UrlUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @Description TODO
 * @author Jane
 * @date 2021/3/29 16:55
 *
 * 1	javax.servlet.error.status_code
 * 该属性给出状态码，状态码可被存储，并在存储为 java.lang.Integer 数据类型后可被分析。
 * 2	javax.servlet.error.exception_type
 * 该属性给出异常类型的信息，异常类型可被存储，并在存储为 java.lang.Class 数据类型后可被分析。
 * 3	javax.servlet.error.message
 * 该属性给出确切错误消息的信息，信息可被存储，并在存储为 java.lang.String 数据类型后可被分析。
 * 4	javax.servlet.error.request_uri
 * 该属性给出有关 URL 调用 Servlet 的信息，信息可被存储，并在存储为 java.lang.String 数据类型后可被分析。
 * 5	javax.servlet.error.exception
 * 该属性给出异常产生的信息，信息可被存储，并在存储为 java.lang.Throwable 数据类型后可被分析。
 * 6	javax.servlet.error.servlet_name
 * 该属性给出 Servlet 的名称，名称可被存储，并在存储为 java.lang.String 数据类型后可被分析。
 *
 * @Copyright
 */

@RestController
public class ErrController implements ErrorController {
    private static final Logger LOGGER =  LogManager.getLogger(ErrController.class);

//    @Override
//    public String getErrorPath() {
//        return null;
//    }
    @RequestMapping(value="/error", produces = { "application/json;charset=UTF-8" })
    public void error(HttpServletRequest request, HttpServletResponse response) throws NoHandlerFoundException, SystemForbiddenException {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        String err_url = (String)request.getAttribute("javax.servlet.error.request_uri");
        String err_msg = (String)request.getAttribute("javax.servlet.error.message");
        LOGGER.debug("method:{},url:{},code:{}",request.getMethod(),UrlUtils.getBaseUrl(request)+err_url,statusCode);
        if(statusCode==404){
            throw new NoHandlerFoundException(request.getMethod(), UrlUtils.getBaseUrl(request)+err_url, HttpHeaders.EMPTY);
        } else if (statusCode==500) {
            LOGGER.error("错误：500");
            throw new SystemForbiddenException();
        } else if(statusCode==400){
            LOGGER.error("错误：400");
            throw new SystemForbiddenException();
        }
    }

    public String getErrorPath() {
        return null;
    }
}
