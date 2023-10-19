package xyz.acproject.security_demo.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import xyz.acproject.datasource_mybatis.entity.BaseEntity;

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
