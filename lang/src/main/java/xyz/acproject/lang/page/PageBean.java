package xyz.acproject.lang.page;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jane
 * @ClassName PageBean
 * @Description TODO
 * @date 2021/2/27 23:59
 * @Copyright:2021
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageBean<T> {
    private List<T> list;
    private Page page;


    public static PageBean build(){
        return new PageBean<>();
    }

    public PageBean<T> list(List<T> list){
        this.list = list;
        return this;
    }

    public PageBean<T> page(Page page){
        this.page = page;
        return this;
    }

    public <C> PageBean<C> copy(List<C> list){
        PageBean<C> pageBean = new PageBean<>();
        pageBean.setList(list);
        pageBean.setPage(this.page);
        return pageBean;
    }

    public static <T> PageBean<T> initPageBean(){
        PageBean<T> pageBean = new PageBean<>();
        pageBean.setList(new ArrayList<>());
        pageBean.setPage(new Page(1,10,0,1));
        return pageBean;
    }
}
