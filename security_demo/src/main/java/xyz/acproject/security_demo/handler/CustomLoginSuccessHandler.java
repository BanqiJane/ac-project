package xyz.acproject.security_demo.handler;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import xyz.acproject.lang.response.Response;
import xyz.acproject.utils.FastJsonUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Jane
 * @ClassName CustomSuccessHandler
 * @Description TODO
 * @date 2021/3/29 16:46
 * @Copyright:2021
 */
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter writer = response.getWriter();
        writer.println(FastJsonUtils.toJson(new Response().add("username",authentication.getPrincipal()).success()));
        writer.flush();
        writer.close();
    }
}
