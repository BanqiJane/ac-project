package xyz.acproject.security_demo.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @author Admin
 * @ClassName UserExcel
 * @Description TODO
 * @date 2023/5/26 11:35
 * @Copyright:2023
 */
@Data
public class UserExcel {

    @ExcelProperty("用户名")
    private String name;

    @ExcelProperty("电话")
    private String phone;


    @ExcelProperty("角色名")
    private String roleName;

    @ExcelProperty("角色标签")
    private String roleTag;

    @ExcelProperty("权限标签")
    private String permissionTag;
}
