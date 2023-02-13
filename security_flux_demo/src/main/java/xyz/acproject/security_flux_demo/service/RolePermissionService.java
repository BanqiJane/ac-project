package xyz.acproject.security_flux_demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.yulichang.base.MPJBaseService;
import xyz.acproject.security_flux_demo.entity.Permission;
import xyz.acproject.security_flux_demo.entity.RolePermission;
import xyz.acproject.datasource_mybatis.service.BaseService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jane
 * @since 2023-01-10
 */
public interface RolePermissionService extends MPJBaseService<RolePermission> {
    List<Permission> listJoinByRoleId(Integer roleId);

    List<Permission> listJoinByUserId(Integer userId);
}
