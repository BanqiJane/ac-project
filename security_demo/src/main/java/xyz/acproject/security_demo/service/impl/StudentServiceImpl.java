package xyz.acproject.security_demo.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import xyz.acproject.datasource_mybatis.service.impl.BaseServiceImpl;
import xyz.acproject.security_demo.dao.StudentDao;
import xyz.acproject.security_demo.entity.Student;
import xyz.acproject.security_demo.service.StudentService;

/**
 * @author Admin
 * @ClassName StudentServiceImpl
 * @Description TODO
 * @date 2023/6/30 14:37
 * @Copyright:2023
 */
@Service
@RequiredArgsConstructor
public class StudentServiceImpl extends BaseServiceImpl<StudentDao, Student> implements StudentService {

}
