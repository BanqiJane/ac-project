package xyz.acproject.security_demo.security;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.context.HttpRequestResponseHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Admin
 * @ClassName CustomSecurityContextRepository
 * @Description TODO
 * @date 2023/5/13 9:26
 * @Copyright:2023
 */
@Component("studentSecurityContextRepository")
public class StudentSecurityContextRepository implements SecurityContextRepository {

    @Resource
    private StudentAuthenticationManager studentAuthenticationManager;

    @Override
    public SecurityContext loadContext(HttpRequestResponseHolder requestResponseHolder) {
        HttpServletRequest request = requestResponseHolder.getRequest();
        String authHeader = request.getHeader("token");
        if(StringUtils.isNotBlank(authHeader)){
            Authentication authentication = new UsernamePasswordAuthenticationToken(authHeader,authHeader);
            return new SecurityContextImpl(this.studentAuthenticationManager.authenticate(authentication));
        }else {
            return SecurityContextHolder.createEmptyContext();
        }
    }

//    @Override
//    public Supplier<SecurityContext> loadContext(HttpServletRequest request) {
//        return SecurityContextRepository.super.loadContext(request);
//    }

    @Override
    public void saveContext(SecurityContext context, HttpServletRequest request, HttpServletResponse response) {
//        System.err.println(context.getAuthentication().getName());
//        throw new AuthException();
    }

    @Override
    public boolean containsContext(HttpServletRequest request) {
        String token = request.getHeader("token");
        if (StringUtils.isNotBlank(token)) {
            return true;
        }
        return false;
    }
}
