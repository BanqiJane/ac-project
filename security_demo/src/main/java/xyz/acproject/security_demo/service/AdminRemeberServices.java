package xyz.acproject.security_demo.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author Jane
 * @ClassName AdminRemeberServices
 * @Description TODO
 * @date 2021/4/6 17:24
 * @Copyright:2021
 */
public class AdminRemeberServices extends TokenBasedRememberMeServices {
    private static final String CURRENT_NAME = "current_name";

    public AdminRemeberServices(String key, UserDetailsService userDetailsService) {
        super(key, userDetailsService);
    }

    @Override
    public void onLoginSuccess(HttpServletRequest request,
                               HttpServletResponse response,
                               Authentication successfulAuthentication) {
        super.onLoginSuccess(request, response, successfulAuthentication);
        SecurityContextHolder.getContext().setAuthentication(
                successfulAuthentication);
        this.afterOnLoginSuccess(request, response, successfulAuthentication);
    }

    private void afterOnLoginSuccess(HttpServletRequest request,
                                     HttpServletResponse response,
                                     Authentication successfulAuthentication) {

        HttpSession session = request.getSession();
        System.out.println("login success-----------------session id = "
                + session.getId());


        String userName = successfulAuthentication.getName();
        //登陆成功时，为新创建出来的空session设置properties,
        session.setAttribute(CURRENT_NAME, userName);
    }

    protected UserDetails processAutoLoginCookie(String[] cookieTokens,
                                                 HttpServletRequest request, HttpServletResponse response) {
        UserDetails userDetails = super.processAutoLoginCookie(cookieTokens,
                request, response);
        this.afterProcessAutoLoginCookie(userDetails, request, response);
        return userDetails;
    }

    private void afterProcessAutoLoginCookie(UserDetails userDetails,
                                             HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(); //之前没有session,</span>在这里会根据新的session id创建新的session
        System.out.println("auto login success-----------------session id = "
                + session.getId());

        String userName = userDetails.getUsername();
        // 当用户已经登陆，直接关闭浏览器，再次又打开浏览器，访问该web应用时候，所走的是： “自动”登陆的流程
        // “自动”登陆成功时，为新创建出来的空session设置properties,
        session.setAttribute(CURRENT_NAME, userName);
    }

}
