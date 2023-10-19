package xyz.acproject.security_demo.service;

import xyz.acproject.datasource_mybatis.service.BaseService;
import xyz.acproject.security_demo.entity.Role;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jane
 * @since 2023-01-10
 */
public interface RoleService extends BaseService<Role> {
    Role getByTag(String tagName);
}
