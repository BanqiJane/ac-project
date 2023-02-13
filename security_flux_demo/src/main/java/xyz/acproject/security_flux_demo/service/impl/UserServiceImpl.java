package xyz.acproject.security_flux_demo.service.impl;

import xyz.acproject.security_flux_demo.entity.User;
import xyz.acproject.security_flux_demo.dao.UserDao;
import xyz.acproject.security_flux_demo.service.UserService;
import xyz.acproject.datasource_mybatis.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author jane
 * @since 2023-01-10
 */
@Service
public class UserServiceImpl extends BaseServiceImpl<UserDao, User> implements UserService {

    public User getByPhone(String phone) {
        List<User> users = this.lambdaQuery().eq(User::getPhone, phone).eq(User::getEnable, true).list();
        if (users.size() > 0) {
            return users.get(0);
        }
        return null;
    }
}
