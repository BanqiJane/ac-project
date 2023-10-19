package xyz.acproject.datasource_mybatis.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.yulichang.base.MPJBaseServiceImpl;
import com.github.yulichang.query.MPJLambdaQueryWrapper;
import com.github.yulichang.query.MPJQueryWrapper;
import xyz.acproject.datasource_mybatis.dao.BaseJoinDao;
import xyz.acproject.datasource_mybatis.entity.BaseEntity;
import xyz.acproject.datasource_mybatis.service.BaseJoinService;
import xyz.acproject.lang.page.Page;
import xyz.acproject.lang.page.PageBean;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Admin
 * @ClassName BaseJoinServiceImpl
 * @Description TODO
 * @date 2023/1/11 9:06
 * @Copyright:2023
 */
public abstract class BaseJoinServiceImpl<D extends BaseJoinDao<T>,T extends BaseEntity> extends MPJBaseServiceImpl<D, T> implements BaseJoinService<T> {

    public Boolean delete(Long id) {
        T entity = this.getById(id);
        if (entity != null) {
            entity.setUpdateTime(LocalDateTime.now());
            return this.removeById(entity);
        }
        return false;
    }

    public List<T> listEnable() {
        QueryWrapper<T> queryWrapper = Wrappers.query();
        queryWrapper.eq("enable", true);
        return this.list(queryWrapper);
    }

    public PageBean<T> pageBean(Page page, MPJQueryWrapper<T> queryWrapper){
        IPage<T> iPage = selectJoinListPage(new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(page.getPageNow(), page.getPageSize()),queryWrapper.getEntityClass(), queryWrapper);
        return new PageBean<>(iPage.getRecords(), page);
    }

    public PageBean<T> pageBeanEnable(Page page, MPJQueryWrapper<T> queryWrapper){
        queryWrapper.eq("enable",true);
        IPage<T> iPage = selectJoinListPage(new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(page.getPageNow(), page.getPageSize()),queryWrapper.getEntityClass(), queryWrapper);
        return new PageBean<>(iPage.getRecords(), page);
    }

    public PageBean<T> pageBean(Page page, MPJLambdaQueryWrapper<T> queryWrapper){
        IPage<T> iPage = selectJoinListPage(new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(page.getPageNow(), page.getPageSize()),queryWrapper.getEntityClass(), queryWrapper);
        return new PageBean<>(iPage.getRecords(), page);
    }

}
