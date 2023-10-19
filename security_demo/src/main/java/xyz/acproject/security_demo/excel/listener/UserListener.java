package xyz.acproject.security_demo.excel.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import xyz.acproject.security_demo.entity.Permission;
import xyz.acproject.security_demo.entity.Role;
import xyz.acproject.security_demo.entity.User;
import xyz.acproject.security_demo.excel.UserExcel;
import xyz.acproject.security_demo.service.*;

/**
 * @author Admin
 * @ClassName UserListener
 * @Description TODO
 * @date 2023/5/26 11:34
 * @Copyright:2023
 */
@Data
@Component
@RequiredArgsConstructor
public class UserListener extends AnalysisEventListener<UserExcel> {

    private final UserService userService;

    private final  RoleService roleService;

    private final  PermissionService permissionService;

    private final  UserRoleService userRoleService;

    private final  RolePermissionService rolePermissionService;

    @Override
    @Transactional
    public void invoke(UserExcel data, AnalysisContext context) {
        User user = userService.getByPhone(data.getPhone());
        if (user == null) {
            user = new User();
            user.setPhone(data.getPhone());
            user.setName("默认用户"+data.getPhone());
            userService.save(user);
        }else{
            user.setName("默认用户"+data.getPhone());
            userService.updateById(user);
        }
        float a = 1/0;
        Role role = roleService.getByTag(data.getRoleTag());
        if(role == null){
            role = new Role();
            role.setName(data.getRoleName());
            role.setNameTag(data.getRoleTag());
            roleService.save(role);
        }else{
            role.setName(data.getRoleName());
            roleService.updateById(role);
        }
        Permission permission = permissionService.getByTag(data.getPermissionTag());
        if(permission == null){
            permission = new Permission();
            permission.setPermissionTag(data.getPermissionTag());
            permissionService.save(permission);
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {

    }
}
