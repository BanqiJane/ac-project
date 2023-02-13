package xyz.acproject.security_demo.security;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import xyz.acproject.security.entity.UserDetail;
import xyz.acproject.security_demo.token.WxAuthenticationToken;
import xyz.acproject.utils.SpringUtils;

/**
 * @author Jane
 * @ClassName WxAuthenticationProvider
 * @Description TODO
 * @date 2021/3/31 17:16
 * @Copyright:2021
 */
public class WxAuthenticationProvider implements AuthenticationProvider {

    private WxUserDetailServiceImpl wxUserDetailService = SpringUtils.getBean(WxUserDetailServiceImpl.class);

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String openId = authentication.getPrincipal().toString();
        final UserDetail userDetail = wxUserDetailService.loadUserByUsername(openId);
        return new WxAuthenticationToken(openId,userDetail.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(WxAuthenticationToken.class);
    }
}
