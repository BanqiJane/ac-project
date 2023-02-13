package xyz.acproject.security_demo.security;

import org.springframework.lang.Nullable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.Assert;
import xyz.acproject.lang.exception.ServerException;
import xyz.acproject.security_demo.token.WxAuthenticationToken;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Jane
 * @ClassName WxAuthenticationFilter
 * @Description TODO
 * @date 2021/3/31 18:07
 * @Copyright:2021
 */
public class WxAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    public static final String SPRING_SECURITY_FORM_TOKEN_KEY = "token";

    private String tokenParameter = SPRING_SECURITY_FORM_TOKEN_KEY;
    private boolean postOnly = false;
    private static final AntPathRequestMatcher DEFAULT_ANT_PATH_REQUEST_MATCHER = new AntPathRequestMatcher("/token","GET");
    private static final AntPathRequestMatcher POST_ANT_PATH_REQUEST_MATCHER = new AntPathRequestMatcher("/login","POST");

    public WxAuthenticationFilter() {
        super(DEFAULT_ANT_PATH_REQUEST_MATCHER);
    }

    public WxAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(DEFAULT_ANT_PATH_REQUEST_MATCHER, authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        if(!postOnly&&!request.getMethod().equals("GET")){
            throw new ServerException("请求方法错误");
        }
        String token = obtainToken(request);
        token = (token != null) ? token : "";
        token = token.trim();
        WxAuthenticationToken authRequest = new WxAuthenticationToken(token);
        // Allow subclasses to set the "details" property
        setDetails(request, authRequest);
        return this.getAuthenticationManager().authenticate(authRequest);
    }


    /**
     * Enables subclasses to override the composition of the username, such as by
     * including additional values and a separator.
     * @param request so that request attributes can be retrieved
     * @return the username that will be presented in the <code>Authentication</code>
     * request token to the <code>AuthenticationManager</code>
     */
    @Nullable
    protected String obtainToken(HttpServletRequest request) {
        return request.getParameter(this.tokenParameter);
    }

    /**
     * Provided so that subclasses may configure what is put into the authentication
     * request's details property.
     * @param request that an authentication request is being created for
     * @param authRequest the authentication request object that should have its details
     * set
     */
    protected void setDetails(HttpServletRequest request, WxAuthenticationToken authRequest) {
        authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));
    }



    public void setTokenParameter(String tokenParameter) {
        Assert.hasText(tokenParameter, "Username parameter must not be empty or null");
        this.tokenParameter = tokenParameter;
    }


    /**
     * Defines whether only HTTP POST requests will be allowed by this filter. If set to
     * true, and an authentication request is received which is not a POST request, an
     * exception will be raised immediately and authentication will not be attempted. The
     * <tt>unsuccessfulAuthentication()</tt> method will be called as if handling a failed
     * authentication.
     * <p>
     * Defaults to <tt>true</tt> but may be overridden by subclasses.
     */
    public void setPostOnly(boolean postOnly) {
        this.postOnly = postOnly;
    }

    public final String getTokenParameter() {
        return this.tokenParameter;
    }

}
