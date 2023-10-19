package xyz.acproject.security_demo.dto;

import lombok.Data;

/**
 * @author Admin
 * @ClassName WaterControlParamDto
 * @Description TODO
 * @date 2023/7/20 19:44
 * @Copyright:2023
 */
@Data
public class WaterControlParamDto {
    private String deviceId;
    private String user_code;
    private String value;
    private Integer operate;
    private Integer protocol_type;
}
