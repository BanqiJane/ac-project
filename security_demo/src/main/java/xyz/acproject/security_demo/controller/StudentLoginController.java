package xyz.acproject.security_demo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.acproject.lang.enums.HttpCodeEnum;
import xyz.acproject.lang.exception.CustomException;
import xyz.acproject.lang.response.Response;
import xyz.acproject.router.controller.Controller;
import xyz.acproject.security.entity.UserDetail;
import xyz.acproject.security.service.JwtService;
import xyz.acproject.security_demo.entity.Student;
import xyz.acproject.security_demo.param.LoginParam;
import xyz.acproject.security_demo.service.StudentService;

import java.util.List;

/**
 * @author Admin
 * @ClassName StudentLoginController
 * @Description TODO
 * @date 2023/6/30 14:25
 * @Copyright:2023
 */
@RestController
@RequestMapping("/student/auth")
@RequiredArgsConstructor
public class StudentLoginController extends Controller {

    private final StudentService studentService;
    private final JwtService jwtService;

    @PostMapping("/login")
    public Response login(@RequestBody LoginParam param){
        List<Student> studentList = studentService.lambdaQuery()
                .eq(Student::getStuNo, param.getAccount())
                .or().eq(Student::getPhone, param.getAccount())
                .list();
        if(studentList.size() == 0){
            return error("账号不存在");
        }
        Student student = studentList.get(0);
        boolean flag =new BCryptPasswordEncoder().matches(param.getPassword(),student.getPassword());
        if(!flag){
            throw new CustomException(HttpCodeEnum.passworderror);
        }
        UserDetail userDetail = UserDetail.builder()
                .username(student.getStuNo())
                .password(student.getPassword())
                .uuid(String.valueOf(student.getId()))
                .authorities("STU")
                .buildSelf();
        final String jwt = jwtService.generateToken(userDetail);
        data("token",jwt);
        return success();
    }
}
