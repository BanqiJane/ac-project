package xyz.acproject.security_flux_demo.security;

import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import xyz.acproject.security_flux.service.JwtService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Jane
 * @ClassName CustomAuthenticationManager
 * @Description TODO
 * @date 2021/4/16 17:18
 * @Copyright:2021
 */
@Component
public class CustomAuthenticationManager implements ReactiveAuthenticationManager {
    @Resource
    private JwtService jwtService;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
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
                }
            }
//            if (a == 0) {
//                throw new SystemForbiddenException();
//            }
            return Mono.just(new UsernamePasswordAuthenticationToken(claims.getSubject(), null, authorities));
        } catch (Exception e) {
            e.printStackTrace();
            return Mono.empty();
        }
    }
}
