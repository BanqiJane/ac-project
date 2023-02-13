package xyz.acproject.security_flux_demo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import xyz.acproject.lang.response.Response;
import xyz.acproject.router_flux.controller.Controller;
import xyz.acproject.security_flux_demo.entity.User;
import xyz.acproject.security_flux_demo.service.PermissionService;
import xyz.acproject.security_flux_demo.service.RoleService;
import xyz.acproject.security_flux_demo.service.UserRoleService;
import xyz.acproject.security_flux_demo.service.UserService;

import javax.annotation.security.PermitAll;
import java.util.concurrent.DelayQueue;

/**
 * @author Admin
 * @ClassName MangerController
 * @Description TODO
 * @date 2023/1/11 10:22
 * @Copyright:2023
 */
@RestController
@RequestMapping("/manager")
@RequiredArgsConstructor
public class MangerController extends Controller {

    private final UserRoleService userRoleService;

    private final UserService userService;

    private final RoleService roleService;

    private final PermissionService permissionService;



    @GetMapping("/user/list")
//    @PreAuthorize("hasRole('MANAGER') or hasRole('SECOND_MANAGER')")
    @PreAuthorize("hasAuthority('user:list')")
    public Mono<Response> user_list(){
        return success(userService.list());
    }

    @PostMapping("/user/save")
    @PreAuthorize("hasAuthority('user:save')")
    public Mono<Response> user_save(){
        return success();
    }


    @PostMapping("/user/delete")
    @PreAuthorize("hasAuthority('user:delete')")
    public Mono<Response> user_delete(){
        return success();
    }

    @GetMapping("/role/list")
//    @PreAuthorize("hasRole('MANAGER')")
    @PreAuthorize("hasAuthority('role:list')")
    public Mono<Response> role_list() {
        return success(roleService.list());
    }

    @PostMapping("/role/save")
    @PreAuthorize("hasAuthority('role:save')")
    public Mono<Response> role_save() {
        return success();
    }

    @PostMapping("/role/delete")
    @PreAuthorize("hasAuthority('role:delete')")
    public Mono<Response> role_delete() {
        return success();
    }

    @PostMapping("/role/bind")
    @PreAuthorize("hasAuthority('role:bind')")
    public Mono<Response> role_bind() {
        return success();
    }



    @GetMapping("/permission/list")
//    @PreAuthorize("hasRole('MANAGER')")
    @PreAuthorize("hasAuthority('permission:list')")
    public Mono<Response> permission_list(){
        return success(permissionService.list());
    }

    @PostMapping("/permission/save")
    @PreAuthorize("hasAuthority('permission:save')")
    public Mono<Response> permission_save(){
        return success();
    }

    @PostMapping("/permission/delete")
    @PreAuthorize("hasAuthority('permission:delete')")
    public Mono<Response> permission_delete(){
        return success();
    }

    @PostMapping("/permission/bind")
    @PreAuthorize("hasAuthority('permission:bind')")
    public Mono<Response> permission_bind(){
        return success();
    }

    @GetMapping("/permission/test1")
    @PermitAll
    public Mono<Response> permission_test1(){
        return success("all");
    }
    @GetMapping("/permission/test2")
    public Mono<Response> permission_test2(){
        return success("tongxing");
    }
    @GetMapping("/permission/test3")
    public Mono<Response> permission_test3(){
        return success("no");
    }


}