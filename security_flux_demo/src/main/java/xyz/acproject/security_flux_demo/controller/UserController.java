package xyz.acproject.security_flux_demo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.acproject.router_flux.controller.Controller;
import xyz.acproject.security_flux_demo.service.UserRoleService;
import xyz.acproject.security_flux_demo.service.UserService;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author jane
 * @since 2023-01-10
 */
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController extends Controller {

    private final UserRoleService userRoleService;

    private final UserService userService;

    public void test(){

    }




}
