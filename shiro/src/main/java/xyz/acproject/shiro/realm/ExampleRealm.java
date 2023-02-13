package xyz.acproject.shiro.realm;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import xyz.acproject.lang.exception.ServiceException;
import xyz.acproject.shiro.token.JwtToken;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Admin
 * @ClassName ExampleRealm
 * @Description TODO
 * @date 2022/12/21 11:06
 * @Copyright:2022
 */

public class ExampleRealm extends AuthorizingRealm {

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        Subject subject = SecurityUtils.getSubject();
        Object principal = subject.getPrincipal();
        if (principal == null) {
            throw new ServiceException("token验证失效");
        }
        String token = (String) principal;
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        //添加权限
//        Long userId = JwtUtil.getId(token);
//        List<AppManMenu> menus = this.appManMenuService.listBySome(userId,2);
//        //region 菜单权限列
//        Set<String> perms = new HashSet<>();//权限列表
//        menus.forEach(menu -> {
//            if (StringUtils.isNotBlank(menu.getPermission())) {
//                perms.add(menu.getPermission());
//            }
//        });
//        simpleAuthorizationInfo.addStringPermissions(perms);
        //endregion
        return simpleAuthorizationInfo;
    }

    /**
     * 验证
     *
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        JwtToken jwtToken = (JwtToken) authenticationToken;
        String token = jwtToken.getToken();
//        if (!JwtUtil.verify(token)) {
//            throw new AuthenticationException("令牌无效，请重新登录");
//        }
//        Long userId = JwtUtil.getId(token);
//        String cacheToken = UserClientUtil.getTokenCache(userId);
//        if (StringUtils.isBlank(cacheToken) || !cacheToken.equals(token)) {
//            throw new AuthenticationException("该账号在另一台设备登陆，请重新登录");
//        }
        return new SimpleAuthenticationInfo(token, token, this.getName());
    }
}
