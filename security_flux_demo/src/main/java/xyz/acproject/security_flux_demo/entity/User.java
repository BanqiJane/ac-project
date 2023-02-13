package xyz.acproject.security_flux_demo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import org.jetbrains.annotations.NotNull;
import xyz.acproject.datasource_mybatis.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

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
