package xyz.acproject.security_flux_demo.service.impl;

import com.github.yulichang.base.MPJBaseServiceImpl;
import com.github.yulichang.toolkit.MPJWrappers;
import org.springframework.stereotype.Service;
import xyz.acproject.security_flux_demo.dao.UserRoleDao;
import xyz.acproject.security_flux_demo.entity.Role;
import xyz.acproject.security_flux_demo.entity.UserRole;
import xyz.acproject.security_flux_demo.service.UserRoleService;

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
public class UserRoleServiceImpl extends MPJBaseServiceImpl<UserRoleDao, UserRole> implements UserRoleService {


    public List<Role> listJoinByUserId(Integer userId){
        return this.selectJoinList(Role.class,
                MPJWrappers.<UserRole>lambdaJoin()
                        .selectAll(Role.class)
                        .leftJoin(Role.class, Role::getId, UserRole::getRoleId)
                        .eq(UserRole::getUserId, userId)
                        .eq(Role::getEnable, true)
        );
    }
}
