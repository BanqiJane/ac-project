package xyz.acproject.security_demo.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import xyz.acproject.datasource_mybatis.service.impl.BaseServiceImpl;
import xyz.acproject.security_demo.dao.RoleDao;
import xyz.acproject.security_demo.entity.Role;
import xyz.acproject.security_demo.service.RoleService;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author jane
 * @since 2023-01-10
 */
@Service
@RequiredArgsConstructor
public class RoleServiceImpl extends BaseServiceImpl<RoleDao, Role> implements RoleService {


    public Role getByTag(String tagName){
        return this.lambdaQuery().eq(Role::getNameTag,tagName).last("limit 1").one();
    }
}
