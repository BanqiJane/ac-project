package xyz.acproject.security_demo.security;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import xyz.acproject.lang.exception.AuthException;
import xyz.acproject.security.entity.UserDetail;

/**
 * @author Jane
 * @ClassName WxUserDetailServiceImpl
 * @Description TODO
 * @date 2021/3/31 18:13
 * @Copyright:2021
 */
@Service
public class WxUserDetailServiceImpl implements UserDetailsService {
    @Override
    public UserDetail loadUserByUsername(String username) throws UsernameNotFoundException {
        if(StringUtils.isBlank(username)) throw new AuthException();
        return UserDetail.builder().username(username).authorities("WX").buildSelf();
    }
}
