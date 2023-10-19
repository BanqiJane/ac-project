package xyz.acproject.datasource_mybatis.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Admin
 * @ClassName BaseEntity
 * @Description TODO
 * @date 2022/12/9 8:42
 * @Copyright:2022
 */
@Data
public abstract class BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;


    @TableId(value = "id", type = IdType.AUTO)
    protected Integer id;

    /**
     * 启用(启用1 未启用0)
     */
    @TableLogic(value = "1", delval = "0")
    protected Boolean enable;

    /**
     * 创建时间
     */

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @TableField(fill = FieldFill.INSERT)
    protected LocalDateTime createTime;

    /**
     * 修改时间
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @TableField(fill = FieldFill.UPDATE)
    protected LocalDateTime updateTime;

    protected String remark;
}
