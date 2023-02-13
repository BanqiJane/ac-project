package xyz.acproject.lang.page;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

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
    private T list;
    private Page page;


    public static PageBean initPageBean(){
        PageBean pageBean = new PageBean();
        pageBean.setList(new ArrayList<>());
        pageBean.setPage(new Page(1,10,0,1));
        return pageBean;
    }
}
