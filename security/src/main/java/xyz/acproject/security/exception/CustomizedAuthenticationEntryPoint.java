package xyz.acproject.security.exception;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import xyz.acproject.lang.enums.HttpCodeEnum;
import xyz.acproject.lang.response.Response;
import xyz.acproject.utils.FastJsonUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Jane
 * @ClassName CustomizedAuthenticationEntryPoint
 * @Description TODO
 * @date 2021/2/19 23:11
 * @Copyright:2021
 */
@Component
public class CustomizedAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final static Logger LOGGER = LogManager.getLogger(CustomizedAuthenticationEntryPoint.class);
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        LOGGER.debug("handle auth:{}",authException.getMessage());
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter writer = response.getWriter();
        writer.println(FastJsonUtils.toJson(new Response().custom(HttpCodeEnum.tokenfail)));
        writer.flush();
        writer.close();
    }
}
