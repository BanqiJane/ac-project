package xyz.acproject.security_demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.acproject.lang.response.Response;

import javax.annotation.security.RolesAllowed;

/**
 * @author Jane
 * @ClassName TestController
 * @Description TODO
 * @date 2021/3/29 15:34
 * @Copyright:2021
 */
@RestController
@RequestMapping("/test")
public class TestController {
    @GetMapping("/test1")
    @RolesAllowed({"ADMIN"})
    public Response method1(){
        return new Response().add("bean","method1").success();
    }

    @GetMapping("/test2")
    @RolesAllowed({"WX"})
    public Response method2(){
        return new Response().add("bean","method2").success();
    }
}
