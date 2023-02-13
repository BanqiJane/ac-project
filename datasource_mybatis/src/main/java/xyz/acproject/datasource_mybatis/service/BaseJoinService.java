package xyz.acproject.datasource_mybatis.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.yulichang.base.MPJBaseService;
import xyz.acproject.datasource_mybatis.entity.BaseEntity;

/**
 * @author Admin
 * @ClassName BaseJoinService
 * @Description TODO
 * @date 2023/1/11 9:05
 * @Copyright:2023
 */
public interface BaseJoinService<T extends BaseEntity> extends MPJBaseService<T> {
}
