package xyz.acproject.security_flux_demo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("role_permission")
public class RolePermission {

    private Integer roleId;

    private Integer permissionId;
}
