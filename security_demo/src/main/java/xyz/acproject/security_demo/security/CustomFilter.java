package xyz.acproject.security_demo.security;

import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import xyz.acproject.security.service.JwtService;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Admin
 * @ClassName CustomFilter
 * @Description TODO
 * @date 2023/5/15 19:53
 * @Copyright:注：注解会经过此方法
 */
@Component
public class CustomFilter extends OncePerRequestFilter {

    @Resource
    private JwtService jwtService;
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        final String authToken = request.getHeader("token");

        if (StringUtils.isBlank(authToken)) {
            filterChain.doFilter(request, response);
            return;
        }
        try {
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
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                    = new UsernamePasswordAuthenticationToken(claims.getSubject(), null, authorities);
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            filterChain.doFilter(request, response);
            return;
        }
    }
}
