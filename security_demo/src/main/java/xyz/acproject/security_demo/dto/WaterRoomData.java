package xyz.acproject.security_demo.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Admin
 * @ClassName WaterRoomData
 * @Description TODO
 * @date 2023/7/20 17:21
 * @Copyright:2023
 */
@Data
public class WaterRoomData {
    private String Id;
    private String Name;
    private String RegionId;
    private String PhysicalAddr;
    private String SerialNo;
    private String ShortUrl;
    private String ParentId;
    private int DeviceType;
    private int DeviceSubType;
    private Date CreateTime;
    private String Position;
    private int UseState;
    private String OriginalSerialNo;
    private String DeviceAddr;
    private String StatusString;
    private String OpenId;
    private String ExtData;
    private int Voltage;
    private int OnlineFlag;

//    @Data
//    private static class StatusString{
//        private Integer semaphore;
//    }
//
//    @Data
//    private static class ExtData{
//        private BigDecimal flow_balance;
//    }

}
