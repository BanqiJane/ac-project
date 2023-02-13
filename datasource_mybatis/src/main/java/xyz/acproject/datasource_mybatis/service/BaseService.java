package xyz.acproject.datasource_mybatis.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import xyz.acproject.datasource_mybatis.entity.BaseEntity;
import xyz.acproject.lang.page.Page;
import xyz.acproject.lang.page.PageBean;

import java.util.List;

/**
 * @author Admin
 * @ClassName BaseService
 * @Description TODO
 * @date 2022/12/9 8:28
 * @Copyright:2022
 */
public interface BaseService<T extends BaseEntity> extends IService<T> {
    Boolean delete(Long id);

    List<T> listEnable();

    PageBean<T> pageBean(Page page, QueryWrapper<T> queryWrapper);

    PageBean<T> pageBeanEnable(Page page, QueryWrapper<T> queryWrapper);
}
