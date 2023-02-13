package xyz.acproject.lang.params;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author Jane
 * @ClassName QueryDateTimeParam
 * @Description TODO
 * @date 2021/9/9 10:51
 * @Copyright:2021
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class QueryDateTimeParam extends QueryBaseParam {
    /**
    * 开始时间
    */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;
    /**
     * 结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;
}
