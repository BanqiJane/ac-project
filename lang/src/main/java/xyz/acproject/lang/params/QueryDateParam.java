package xyz.acproject.lang.params;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author Jane
 * @ClassName QueryDateParam
 * @Description TODO
 * @date 2021/9/9 10:52
 * @Copyright:2021
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class QueryDateParam extends QueryBaseParam{
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endTime;
}
