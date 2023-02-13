package xyz.acproject.security_demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import xyz.acproject.lang.exception.AuthException;
import xyz.acproject.lang.response.Response;
import xyz.acproject.security.entity.UserDetail;
import xyz.acproject.security_demo.security.UserDetailServiceImpl;
import xyz.acproject.security_demo.security.WxUserDetailServiceImpl;
import xyz.acproject.security_demo.token.WxAuthenticationToken;

/**
 * @author Jane
 * @ClassName IndexController
 * @Description TODO
 * @date 2021/3/28 16:35
 * @Copyright:2021
 */
@RestController
@RequestMapping("/admin")
public class IndexController {
    private UserDetailServiceImpl userDetailService;
    private AuthenticationManager authenticateManager;
    private WxUserDetailServiceImpl wxUserDetailService;
    @GetMapping("/login")
    public Response login(@RequestParam("username") String username,@RequestParam("password") String password){
        final UserDetail userDetail = userDetailService.loadUserByUsername(username);
        Authentication authentication;
        try {
            authentication = authenticateManager.authenticate(new UsernamePasswordAuthenticationToken(username, password,userDetail.getAuthorities()));
        }catch(Exception e){
            if(e instanceof BadCredentialsException){
                throw new AuthException();
            }
            e.printStackTrace();
        }
        return new Response().add("token",new UsernamePasswordAuthenticationToken(username,password)).success();
    }
    @GetMapping("/token")
    public Response login(@RequestParam("token") String token){
        final UserDetail userDetail = wxUserDetailService.loadUserByUsername(token);
        Authentication authentication;
        try {
            authentication = authenticateManager.authenticate(new WxAuthenticationToken(token,userDetail.getAuthorities()));
        }catch(Exception e){
            if(e instanceof BadCredentialsException){
                throw new AuthException();
            }
            e.printStackTrace();
        }
        return new Response().add("token",new WxAuthenticationToken(token)).success();
    }

    @Autowired
    public void setAuthenticateManager(AuthenticationManager authenticateManager) {
        this.authenticateManager = authenticateManager;
    }

    @Autowired
    public void setUserDetailService(UserDetailServiceImpl userDetailService) {
        this.userDetailService = userDetailService;
    }
    @Autowired
    public void setWxUserDetailService(WxUserDetailServiceImpl wxUserDetailService) {
        this.wxUserDetailService = wxUserDetailService;
    }

}
