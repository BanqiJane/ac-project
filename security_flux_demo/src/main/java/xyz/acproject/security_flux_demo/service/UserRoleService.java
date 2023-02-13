package xyz.acproject.security_flux_demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.yulichang.base.MPJBaseService;
import xyz.acproject.security_flux_demo.entity.Role;
import xyz.acproject.security_flux_demo.entity.UserRole;
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
public interface UserRoleService extends MPJBaseService<UserRole> {
    List<Role> listJoinByUserId(Integer userId);
}
