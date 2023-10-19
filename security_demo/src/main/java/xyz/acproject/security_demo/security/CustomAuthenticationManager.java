package xyz.acproject.security_demo.security;

import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import xyz.acproject.lang.exception.SystemForbiddenException;
import xyz.acproject.security.service.JwtService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Admin
 * @ClassName CustomAuthenticationManager
 * @Description TODO
 * @date 2023/5/12 20:40
 * @Copyright:2023
 */
@Component("customAuthenticationManager")
public class CustomAuthenticationManager implements AuthenticationManager {

    @Resource
    private JwtService jwtService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        try {
            final String authToken = authentication.getCredentials().toString();
//            final String name  = jwtService.extractUsername(authToken);
            final Claims claims = jwtService.extractAllClaims(authToken);
            final List<String> rolesMap = claims.get("role", List.class);
            final List<GrantedAuthority> authorities = new ArrayList<>();
            int a = 0;
            if(rolesMap!=null) {
                for (String rolemap : rolesMap) {
                    authorities.add(new SimpleGrantedAuthority(rolemap));
                    if (rolemap.equals("USER")) {
                        a ++;
                    }
                }
            }
            if (a == 0) {
                throw new SystemForbiddenException();
            }
            return new UsernamePasswordAuthenticationToken(claims.getSubject(), null, authorities);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
