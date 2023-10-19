package xyz.acproject.security_flux_demo.service.impl;

import com.github.yulichang.base.MPJBaseServiceImpl;
import com.github.yulichang.toolkit.MPJWrappers;
import org.springframework.stereotype.Service;
import xyz.acproject.security_flux_demo.dao.RolePermissionDao;
import xyz.acproject.security_flux_demo.entity.Permission;
import xyz.acproject.security_flux_demo.entity.RolePermission;
import xyz.acproject.security_flux_demo.entity.UserRole;
import xyz.acproject.security_flux_demo.service.RolePermissionService;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author jane
 * @since 2023-01-10
 */
@Service
public class RolePermissionServiceImpl extends MPJBaseServiceImpl<RolePermissionDao, RolePermission> implements RolePermissionService {

    public List<Permission> listJoinByRoleId(Integer roleId){
        return this.selectJoinList(Permission.class,
                MPJWrappers.<RolePermission>lambdaJoin()
                        .selectAll(Permission.class)
                        .leftJoin(Permission.class, Permission::getId, RolePermission::getRoleId)
                        .eq(UserRole::getUserId, roleId)
                        .eq(Permission::getEnable, true)
        );
    }
    public List<Permission> listJoinByUserId(Integer userId){
        return this.selectJoinList(Permission.class,
                MPJWrappers.<RolePermission>lambdaJoin()
                        .selectAll(Permission.class)
                        .leftJoin(Permission.class, Permission::getId, RolePermission::getPermissionId)
                        .leftJoin(UserRole.class, UserRole::getRoleId, RolePermission::getRoleId)
                        .eq(UserRole::getUserId, userId)
                        .eq(Permission::getEnable, true)
        );
    }
}
