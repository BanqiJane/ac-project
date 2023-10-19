package xyz.acproject.security_flux_demo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import xyz.acproject.lang.response.Response;
import xyz.acproject.router_flux.controller.Controller;
import xyz.acproject.security_flux.entity.UserDetail;
import xyz.acproject.security_flux.service.JwtService;
import xyz.acproject.security_flux_demo.entity.Permission;
import xyz.acproject.security_flux_demo.entity.User;
import xyz.acproject.security_flux_demo.service.RolePermissionService;
import xyz.acproject.security_flux_demo.service.UserRoleService;
import xyz.acproject.security_flux_demo.service.UserService;

import java.util.List;

/**
 * @author Admin
 * @ClassName AuthController
 * @Description TODO
 * @date 2023/1/11 9:40
 * @Copyright:2023
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController extends Controller {

    private final UserRoleService userRoleService;

    private final UserService userService;

    private final JwtService jwtService;

    private final RolePermissionService rolePermissionService;


    @GetMapping("/test")
    public Mono<Response> test(String phone){
        User user = userService.getByPhone(phone);
        if (user == null) {
            user = new User();
            user.setName("默认用户");
            user.setPhone(phone);
            userService.saveOrUpdate(user);
        }
//        List<Role> roles = userRoleService.listJoinByUserId(user.getId());
        List<Permission> permissions = rolePermissionService.listJoinByUserId(user.getId());
        UserDetail userDetail = UserDetail.builder()
                .username(String.valueOf(user.getId()))
                .password(user.getPhone())
                .uuid(String.valueOf(user.getId()))
                .authorities(permissions.stream().map(Permission::getPermissionTag).toArray(String[]::new))
                .buildSelf();
        final String jwt = jwtService.generateToken(userDetail);
        data("token", jwt);
        data("user", user);
        return success();
    }


}
