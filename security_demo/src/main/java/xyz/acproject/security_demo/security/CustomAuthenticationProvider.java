package xyz.acproject.security_demo.security;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import xyz.acproject.security.entity.UserDetail;
import xyz.acproject.utils.SpringUtils;

/**
 * @author Jane
 * @ClassName CustomAuthenticationProvider
 * @Description TODO
 * @date 2021/3/29 11:14
 * @Copyright:2021
 *
 * 使用请bean注入
 */
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private UserDetailServiceImpl userDetailService = SpringUtils.getBean(UserDetailServiceImpl.class);


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        final UserDetail userDetail = userDetailService.loadUserByUsername(username);
        //数据对比
        if(userDetail.getUsername().equals(username)&&userDetail.getPassword().equals(password)){
            return new UsernamePasswordAuthenticationToken(username,password, userDetail.getAuthorities());
        }else{
            throw new BadCredentialsException("account login failed");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
