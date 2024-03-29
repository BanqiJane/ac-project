package xyz.acproject.security_flux_demo.service.impl;

import org.springframework.stereotype.Service;
import xyz.acproject.datasource_mybatis.service.impl.BaseServiceImpl;
import xyz.acproject.security_flux_demo.dao.RoleDao;
import xyz.acproject.security_flux_demo.entity.Role;
import xyz.acproject.security_flux_demo.service.RoleService;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author jane
 * @since 2023-01-10
 */
@Service
public class RoleServiceImpl extends BaseServiceImpl<RoleDao, Role> implements RoleService {

}
