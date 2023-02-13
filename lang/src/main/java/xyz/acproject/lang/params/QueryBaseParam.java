package xyz.acproject.lang.params;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Jane
 * @ClassName QueryBaseParam
 * @Description TODO
 * @date 2021/3/25 12:09
 * @Copyright:2021
 */
@Data
public class QueryBaseParam {

    /**
    * 通用id
    */
    private Integer id;
    /**
    * 关键字
    */
    private String keyword;
    /**
     * 当前页
     */
    private Integer pageNow;
    /**
     * 每页多少条
     */
    private Integer pageSize;
    /**
     * 查询排序字段
     */
    private String property = "id";
    /**
    * 排序方式 desc & asc
    */
    private String order = "DESC";
    /**
    * 查询是否有效的
    */
    private Boolean enable;

    public String getProperty() {
        if(this.property==null||this.property.trim()==""){
            return "id";
        }
        return property;
    }

    public List<String> getPropertys(){
        String property = getProperty();
        if(property.contains(",")){
            List<String> strings = Arrays.asList(StringUtils.split(property, ","));
            return strings;
        }
        List<String> strings = new ArrayList<>();
        strings.add(property);
        return strings;
    }

    public String getOrder() {
        if(this.order==null||this.order.trim()==""){
            return "desc";
        }
        return order;
    }

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
