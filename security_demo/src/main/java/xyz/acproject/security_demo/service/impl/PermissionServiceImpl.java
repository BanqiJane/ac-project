package xyz.acproject.security_demo.service.impl;

import org.springframework.stereotype.Service;
import xyz.acproject.datasource_mybatis.service.impl.BaseServiceImpl;
import xyz.acproject.security_demo.dao.PermissionDao;
import xyz.acproject.security_demo.entity.Permission;
import xyz.acproject.security_demo.service.PermissionService;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author jane
 * @since 2023-01-10
 */
@Service
public class PermissionServiceImpl extends BaseServiceImpl<PermissionDao, Permission> implements PermissionService {

    public Permission getByTag(String tagName){
        return this.lambdaQuery().eq(Permission::getPermissionTag,tagName).last("limit 1").one();
    }
}
