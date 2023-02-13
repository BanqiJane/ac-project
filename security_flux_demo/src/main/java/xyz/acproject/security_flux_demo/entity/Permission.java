package xyz.acproject.security_flux_demo.entity;

import xyz.acproject.datasource_mybatis.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author jane
 * @since 2023-01-10
 */
@Getter
@Setter
@Accessors(chain = true)
public class Permission extends BaseEntity {

    private String permissionTag;

    private Integer parentId;
}
