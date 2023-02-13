package xyz.acproject.utils.entity.param;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.coobird.thumbnailator.geometry.Positions;

import java.io.File;

/**
 * @author Jane
 * @ClassName ImageParam
 * @Description TODO
 * @date 2022/5/7 9:38
 * @Copyright:2022
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ImageParam {
    /**
    * 宽 不一定需要
    */
    private Integer width;
    /**
     * 高 不一定需要
     */
    private Integer height;
    /**
     * 需要转接图片后缀 本项目不支持webp
     */
    @Builder.Default
    private String suffix = "jpg";

    /**
    * 保持宽高比例 默认保持
    */
    @Builder.Default
    private boolean keepAspectRatio = true;


    /**
    * 图片压缩质量  默认 1 为最高
    */
    @Builder.Default
    private Double quality = 1d;


    /**
    * 处理类型 CROP为裁剪 RESIZE 为默认放大 缩小
    */
    @Builder.Default
    private HandleType handleType = HandleType.RESIZE;

    /**
    * 裁剪区域 默认正中心 裁剪模式下有效
    */
    @Builder.Default
    private Positions positions = Positions.CENTER;

    /**
    * 裁剪横坐标 裁剪模式下有效
    */
    private Integer x;

    /**
    * 裁剪纵坐标 裁剪模式下有效
    */
    private Integer y;


    /**
     * 裁剪宽 裁剪模式下有效
     */
    private Integer cw;

    /**
     * 裁剪高 裁剪模式下有效
     */
    private Integer ch;


    /**
    * 保持比例缩放 跟width冲突 所以没有默认
    */
    private Double scale;

    /**
    * 旋转 默认不旋转
    */
    @Builder.Default
    private Double rotate = 0d;


    //以后预留的水印

    public enum HandleType{
        CROP,RESIZE;
    }

}
