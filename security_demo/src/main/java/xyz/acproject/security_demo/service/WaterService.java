package xyz.acproject.security_demo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import xyz.acproject.cache.RedisService;
import xyz.acproject.security_demo.dto.WaterControlParamDto;
import xyz.acproject.security_demo.dto.WaterRecordDto;
import xyz.acproject.security_demo.dto.WaterRoomData;
import xyz.acproject.security_demo.http.WaterHttp;

import java.util.List;

/**
 * @author Admin
 * @ClassName WaterService
 * @Description TODO
 * @date 2023/7/21 10:01
 * @Copyright:2023
 */
@Service
@RequiredArgsConstructor
public class WaterService {
    private final RedisService redisService;

    public String getToken(boolean refresh){
        String token = redisService.get("water_token");
        if (token == null || refresh){
            token = WaterHttp.getToken();
            redisService.set("water_token",token,7200);
        }
        return token;
    }

    public List<WaterRoomData>  getWaterData(String regionId){
        return WaterHttp.getWaterData(getToken(false),regionId);
    }

    public List<WaterRoomData>  getPowerData(String regionId){
        return WaterHttp.getPowerData(getToken(false),regionId);
    }

    public String controlWater(WaterControlParamDto waterControlParamDto){
        return WaterHttp.controlWater(getToken(false),waterControlParamDto);
    }

    public WaterRecordDto parseData(String code){
        return WaterHttp.parseData(getToken(false),code);
    }





}
