package xyz.acproject.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Jane
 * @ClassName PageUtils
 * @Description TODO
 * @date 2022/3/14 15:54
 * @Copyright:2022
 */
public class PageUtils {



    public static PageUtils.Page parse(Integer pageNow,Integer pageSize){
        PageUtils.Page page = new PageUtils.Page();
        if(pageNow<=1||pageNow==null){
        }else{
            pageNow = (pageNow-1)*pageSize;
        }
        page.setStart(pageNow<=1?0:pageNow);
        page.setCount(pageSize);
        return page;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Page{
        private Integer start;
        private Integer count;

    }
}
