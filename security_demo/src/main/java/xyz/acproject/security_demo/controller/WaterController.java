package xyz.acproject.security_demo.controller;

import com.alibaba.fastjson.JSONObject;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import xyz.acproject.lang.response.Response;
import xyz.acproject.router.controller.Controller;
import xyz.acproject.security_demo.dto.WaterControlParamDto;
import xyz.acproject.security_demo.http.WaterHttp;
import xyz.acproject.security_demo.service.WaterService;
import xyz.acproject.utils.FastJsonUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Admin
 * @ClassName WaterController
 * @Description TODO
 * @date 2023/7/21 9:57
 * @Copyright:2023
 */
@RestController
@RequiredArgsConstructor
public class WaterController extends Controller {

    private final WaterService waterService;

    @GetMapping("/getWaterByRegionId")
    public Response<?> method7(String regionId){
        return new Response<>().success().data(waterService.getWaterData(regionId));
    }

    @GetMapping("/getPowerByRegionId")
    public Response<?> method7p5(String regionId){
        return new Response<>().success().data(waterService.getPowerData(regionId));
    }

    @PostMapping("/controlWater")
    public Response<?> method8(@RequestBody WaterControlParamDto waterControlParamDto){
        return new Response<>().success().data(JSONObject.parse(waterService.controlWater(waterControlParamDto)));
    }

    @GetMapping("Record/DealFlowControlRecord")
    public Map<String,Object> method9(String deviceId,String physicalAddr,String serialNo,String code,String commandType){
        System.err.println(deviceId);
        System.err.println(physicalAddr);
        System.err.println(serialNo);
        System.err.println(code);
        System.err.println(commandType);
        System.err.println(waterService.parseData(code));
        Map<String,Object> map = new HashMap<>();
        map.put("Code",10000);
        map.put("Msg","");
        map.put("Data","");
        return map;
    }

    @GetMapping("/getDoorS")
    public Response<?> method11(){
        return new Response<>().success().data(WaterHttp.getDoorSData(waterService.getToken(false),""));
    }


    @GetMapping("/getDoorJ")
    public Response<?> method12(){
        return new Response<>().success().data(WaterHttp.getDoorJData(waterService.getToken(false),""));
    }

    @GetMapping("parse")
    public Response<?> method10(String code){
        return new Response<>().success().data(waterService.parseData(code));
    }

    @GetMapping("getTaskData")
    public Response<?> method13(String taskId){
        return new Response<>().success().data(WaterHttp.getTaskData(taskId));
    }
}
