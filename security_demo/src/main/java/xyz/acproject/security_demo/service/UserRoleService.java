package xyz.acproject.security_demo.service;

import com.github.yulichang.base.MPJBaseService;
import xyz.acproject.security_demo.entity.Role;
import xyz.acproject.security_demo.entity.UserRole;

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
