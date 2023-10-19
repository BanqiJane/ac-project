package xyz.acproject.security_demo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.acproject.lang.response.Response;
import xyz.acproject.router.controller.Controller;
import xyz.acproject.security.entity.UserDetail;
import xyz.acproject.security.service.JwtService;
import xyz.acproject.security_demo.entity.Permission;
import xyz.acproject.security_demo.entity.User;
import xyz.acproject.security_demo.service.RolePermissionService;
import xyz.acproject.security_demo.service.UserService;

import java.util.List;

/**
 * @author Admin
 * @ClassName LoginController
 * @Description TODO
 * @date 2023/5/13 15:19
 * @Copyright:2023
 */
@RestController
@RequestMapping("/login")
@RequiredArgsConstructor
public class LoginController extends Controller {

    private final UserService userService;
    private final RolePermissionService rolePermissionService;
    private final JwtService jwtService;
    @GetMapping("oauth")
    public Response login(String phone){
        User user = userService.getByPhone(phone);
        if (user == null) {
            user = new User();
            user.setName("默认用户");
            user.setPhone(phone);
            userService.saveOrUpdate(user);
        }
//        List<Role> roles = userRoleService.listJoinByUserId(user.getId());
//        List<Permission> permissions = rolePermissionService.listJoinByUserId(user.getId());
        UserDetail userDetail = UserDetail.builder()
                .username(String.valueOf(user.getId()))
                .password(user.getPhone())
                .uuid(String.valueOf(user.getId()))
                .authorities("USER")
//                .authorities(permissions.stream().map(Permission::getPermissionTag).toArray(String[]::new))
                .buildSelf();
        final String jwt = jwtService.generateToken(userDetail);
        data("token", jwt);
        data("user", user);
        return success();
    }
}
