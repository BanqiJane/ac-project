package xyz.acproject.security_demo.security;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import xyz.acproject.security.entity.UserDetail;

/**
 * @author Jane
 * @ClassName UserDetailServiceImpl
 * @Description 数据库对比并赋予权限
 * @date 2021/3/29 12:00
 * @Copyright:2021
 */
@Service
public class UserDetailServiceImpl implements UserDetailsService {
    @Override
    public UserDetail loadUserByUsername(String username) throws UsernameNotFoundException {
        if(StringUtils.isBlank(username))throw new InternalAuthenticationServiceException("用户名非空");
        //模拟数据库
        //...这里省略了从数据库拿出账号密码
        return UserDetail.builder().username(username).password("123456").authorities("ADMIN").buildSelf();
    }
}
