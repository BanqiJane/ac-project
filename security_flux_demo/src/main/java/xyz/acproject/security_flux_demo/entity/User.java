package xyz.acproject.security_flux_demo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import xyz.acproject.datasource_mybatis.entity.BaseEntity;

import java.util.List;

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
public class User extends BaseEntity {

    private String name;

    private String phone;

    @TableField(exist = false)
    private List<String> roles;


    @TableField(exist = false)
    private List<String> permissions;

}
