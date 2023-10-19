package xyz.acproject.lang.params;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Jane
 * @ClassName QueryPageParam
 * @Description TODO
 * @date 2021/12/7 14:44
 * @Copyright:2021
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QueryPageParam {
    /**
     * 当前页
     */
    private Integer pageNow;
    /**
     * 每页多少条
     */
    private Integer pageSize;

    public Integer getPageNow() {
        if(pageNow==null||pageNow<=0){
            return 1;
        }
        return pageNow;
    }

    public Integer getPageSize() {
        if(pageSize==null||pageSize<=0){
            return 10;
        }
        return pageSize;
    }
}
