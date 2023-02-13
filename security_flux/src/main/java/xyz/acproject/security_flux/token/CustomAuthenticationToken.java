package xyz.acproject.security_flux.token;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * @author Jane
 * @ClassName CustomAuthenticationToken
 * @Description TODO
 * @date 2021/3/29 12:28
 * @Copyright:2021
 */
public class CustomAuthenticationToken extends UsernamePasswordAuthenticationToken {
    public CustomAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }
}
