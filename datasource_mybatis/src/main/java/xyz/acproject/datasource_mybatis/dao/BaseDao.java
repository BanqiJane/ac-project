package xyz.acproject.datasource_mybatis.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import xyz.acproject.datasource_mybatis.entity.BaseEntity;

/**
 * @author Admin
 * @ClassName BaseDao
 * @Description TODO
 * @date 2022/12/9 8:49
 * @Copyright:2022
 */
public interface BaseDao<T extends BaseEntity> extends BaseMapper<T> {

}
