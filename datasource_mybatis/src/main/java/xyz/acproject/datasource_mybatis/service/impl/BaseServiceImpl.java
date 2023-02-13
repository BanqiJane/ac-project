package xyz.acproject.datasource_mybatis.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import xyz.acproject.datasource_mybatis.dao.BaseDao;
import xyz.acproject.datasource_mybatis.entity.BaseEntity;
import xyz.acproject.datasource_mybatis.service.BaseService;
import xyz.acproject.lang.page.Page;
import xyz.acproject.lang.page.PageBean;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Admin
 * @ClassName BaseServiceImpl
 * @Description TODO
 * @date 2022/12/9 8:48
 * @Copyright:2022
 */
public abstract class BaseServiceImpl<D extends BaseDao<T>,T extends BaseEntity> extends ServiceImpl<D, T> implements BaseService<T> {


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

    public PageBean<T> pageBean(Page page, QueryWrapper<T> queryWrapper){
        IPage<T> iPage = page(new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(page.getPageNow(), page.getPageSize()), queryWrapper);
        return new PageBean(iPage.getRecords(), page);
    }

    public PageBean<T> pageBeanEnable(Page page, QueryWrapper<T> queryWrapper){
        queryWrapper.eq("enable",true);
        IPage<T> iPage = page(new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(page.getPageNow(), page.getPageSize()), queryWrapper);
        return new PageBean(iPage.getRecords(), page);
    }


}
