package xyz.acproject.security_flux_demo.service.impl;

import xyz.acproject.security_flux_demo.entity.Permission;
import xyz.acproject.security_flux_demo.dao.PermissionDao;
import xyz.acproject.security_flux_demo.service.PermissionService;
import xyz.acproject.datasource_mybatis.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;

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

}
