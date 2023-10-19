package xyz.acproject.security_demo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import xyz.acproject.datasource_mybatis.entity.BaseEntity;

/**
 * @author Admin
 * @ClassName Student
 * @Description TODO
 * @date 2023/5/11 9:26
 * @Copyright:2023
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Student extends BaseEntity {

    private String name;

    private String stuNo;

    private String phone;

    private String password;

    public Student(String name) {
        this.name = name;
    }
}
