package xyz.acproject.security_demo.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * @author Admin
 * @ClassName UserDetailsServiceImpl
 * @Description TODO
 * @date 2023/5/16 8:58
 * @Copyright:2023
 */
public class UserDetailsServiceImpl implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }
}
