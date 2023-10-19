package xyz.acproject.security_flux_demo.service;

import xyz.acproject.datasource_mybatis.service.BaseService;
import xyz.acproject.security_flux_demo.entity.User;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jane
 * @since 2023-01-10
 */
public interface UserService extends BaseService<User> {
    User getByPhone(String phone);
}
