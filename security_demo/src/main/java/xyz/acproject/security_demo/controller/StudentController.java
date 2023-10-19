package xyz.acproject.security_demo.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import xyz.acproject.lang.response.Response;
import xyz.acproject.router.controller.Controller;
import xyz.acproject.security_demo.entity.Student;
import xyz.acproject.security_demo.service.StudentService;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Admin
 * @ClassName StudentController
 * @Description TODO
 * @date 2023/6/30 14:53
 * @Copyright:2023
 */
@RestController
@RequestMapping("/student")
@RequiredArgsConstructor
public class StudentController extends Controller {

    private final static Logger LOGGER = LogManager.getLogger(StudentController.class);


    private final StudentService studentService;

    @GetMapping("page")
    public Response page(){
        return success("page");
    }

    @GetMapping("list")
    public Response list(){
        return success("list");
    }

    @PostMapping("saveOne")
    public Response saveOne(@RequestBody Student student){
//        boolean flag = studentService.save(student);
        return success(student);
    }

    @GetMapping("save")
    public Response save(){
        List<Student> studentList = new ArrayList<>();
        for(int i =1;i<10000;i++){
            Student student = new Student();
            student.setStuNo("no"+i);
            student.setName("张三"+i);
            student.setPassword(new BCryptPasswordEncoder().encode("123456"));
            studentList.add(student);
        }
        studentList.stream().parallel().forEach(student -> {
             boolean flag = studentService.save(student);
            LOGGER.info("导入flag:{}，数据:{}",flag,student);

        });
        return success("save");
    }
}
