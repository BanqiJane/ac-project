package xyz.acproject.security.token;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.util.Assert;

import java.util.Collection;

/**
 * @author Jane
 * @ClassName WxAuthenticationToken
 * @Description TODO
 * @date 2021/3/31 17:44
 * @Copyright:2021
 */
public class WxAuthenticationToken extends AbstractAuthenticationToken {

    private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

    private final Object openId;

    private Object credentials = null;

    public WxAuthenticationToken(Object openId) {
        super(null);
        this.openId = openId;
        setAuthenticated(false);
    }

    public WxAuthenticationToken(Object openId,
                                 Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.openId = openId;
        super.setAuthenticated(true); // must use super, as we override
    }
    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return this.openId;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        Assert.isTrue(!isAuthenticated,
                "Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        super.setAuthenticated(false);
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
        this.credentials = null;
    }
}
