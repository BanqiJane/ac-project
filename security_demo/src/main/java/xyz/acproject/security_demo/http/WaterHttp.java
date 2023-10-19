package xyz.acproject.security_demo.http;

import cn.hutool.core.net.url.UrlBuilder;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import xyz.acproject.lang.response.Response;
import xyz.acproject.security_demo.dto.WaterControlParamDto;
import xyz.acproject.security_demo.dto.WaterRecordDto;
import xyz.acproject.security_demo.dto.WaterRoomData;
import xyz.acproject.utils.http.OkHttp3Utils;
import xyz.acproject.utils.http.OkHttpClientUtil;
import xyz.acproject.utils.http.OkHttpUtils;

import java.util.List;

/**
 * @author Admin
 * @ClassName WaterHttp
 * @Description TODO
 * @date 2023/7/20 16:26
 * @Copyright:2023
 */

public class WaterHttp {
//    static String CHARGING_BASE_URL = "http://120.76.233.84:8091";
////    // 账号
//    static String CHARGING_ACCOUNT = "18888888888";
////    // 密码
//     static String CHARGING_PASSWORD = "as1234";
    static String CHARGING_BASE_URL = "https://smartdiy2.suozhang.net:4430";
    // 账号
    static String CHARGING_ACCOUNT = "18888889999";

    static String CHARGING_PASSWORD = "889999";

//    static String CHARGING_ACCOUNT = "15815815815";
//    // 密码
//    static String CHARGING_PASSWORD = "815815";

    static String waterDeviceSubType = "0";
    static String powerDeviceSubType = "1";

    public static String getToken(){
        String token = OkHttpUtils.builder().url(CHARGING_BASE_URL + "/Token/GetToken")
                .addHeader("account",CHARGING_ACCOUNT).addHeader("password",CHARGING_PASSWORD)
                .get().sync();
        System.out.println("tokenReturn:"+token);
        JSONObject tokenJson = JSONObject.parseObject(token);
        String tokenStr = tokenJson.getString("Data");
        System.out.println("tokenStr:"+tokenStr);
        return tokenStr;
    }

    public static String getTaskData(String taskId) {
        String url = CHARGING_BASE_URL + "/ThirdParty/GetTaskData";
        String token = getToken();
        String taskData = OkHttpUtils.builder().url(url)
                .addHeader("token",token)
                .addParam("taskId",taskId)
                .get().sync();
        System.out.println("taskData:"+taskData);
        return taskData;
    }



    public static List<WaterRoomData> getPowerData(String token,String regionId){
        String url = CHARGING_BASE_URL + "/ThirdParty/GetDeviceData";
        String waterData = OkHttpUtils.builder().url(url)
                .addHeader("token",token)
                .addParam("regionId",regionId)
                .addParam("deviceType","4")
                .addParam("deviceSubType",powerDeviceSubType)
                .get().sync();
        System.out.println("waterData:"+waterData);
        JSONObject jsonObject = JSON.parseObject(waterData);
        List<WaterRoomData> waterRoomDataList = JSON.parseArray(jsonObject.getString("Data"), WaterRoomData.class);
        return waterRoomDataList;
    }


    public static List<WaterRoomData> getWaterData(String token,String regionId){
        String url = CHARGING_BASE_URL + "/ThirdParty/GetDeviceData";
        String waterData = OkHttpUtils.builder().url(url)
                .addHeader("token",token)
                .addParam("regionId",regionId)
                .addParam("deviceType","4")
                .addParam("deviceSubType",waterDeviceSubType)
                .get().sync();
        System.out.println("waterData:"+waterData);
        JSONObject jsonObject = JSON.parseObject(waterData);
        List<WaterRoomData> waterRoomDataList = JSON.parseArray(jsonObject.getString("Data"), WaterRoomData.class);
        return waterRoomDataList;
    }


    public static JSONArray getDoorSData(String token, String regionId){
        String url = CHARGING_BASE_URL + "/ThirdParty/GetDeviceData";
        String waterData = OkHttpUtils.builder().url(url)
                .addHeader("token",token)
                .addParam("regionId",regionId)
                .addParam("deviceType","0")
                .addParam("deviceSubType",-1)
                .get().sync();
        JSONObject jsonObject = JSON.parseObject(waterData);
        JSONArray jsonArray =  JSON.parseArray(jsonObject.getString("Data"));
        System.err.println(jsonArray.size());
        return jsonArray;
    }

    public static JSONArray getDoorJData(String token, String regionId){
        String url = CHARGING_BASE_URL + "/ThirdParty/GetDeviceData";
        String waterData = OkHttpUtils.builder().url(url)
                .addHeader("token",token)
                .addParam("regionId",regionId)
                .addParam("deviceType","8")
                .addParam("deviceSubType",-1)
                .get().sync();
        JSONObject jsonObject = JSON.parseObject(waterData);
        JSONArray jsonArray =  JSON.parseArray(jsonObject.getString("Data"));
        System.err.println(jsonArray.size());
        return jsonArray;
    }

    public static String controlWater(String token, WaterControlParamDto waterControlParamDto){
        String url = CHARGING_BASE_URL + "/ThirdParty/SmartCampus/FlowDeviceControl?deviceId="+waterControlParamDto.getDeviceId();
        String waterData = OkHttpUtils.builder().url(url)
                .addHeader("token",token)
                .addHeader("device_id",waterControlParamDto.getDeviceId())
                .addParam("user_code",waterControlParamDto.getUser_code())
                .addParam("value",waterControlParamDto.getValue())
                .addParam("operate",waterControlParamDto.getOperate())
//                .addParam("device_id",waterControlParamDto.getDeviceId())
                .addParam("protocol_type",waterControlParamDto.getProtocol_type())
                .postJson().sync();
        System.out.println("waterData:"+waterData);
        return waterData;
    }

    public static WaterRecordDto parseData(String token,String code){
        String url = CHARGING_BASE_URL + "/ThirdParty/SmartCampus/GetFlowControlCodeInfo";
        String parseControlData = OkHttpUtils.builder().url(url)
                .addHeader("token",token)
                .addParam("code",code)
                .get().sync();
        System.out.println("parseControlData:"+parseControlData);
        String data = JSONObject.parseObject(parseControlData).getString("Data");
        WaterRecordDto waterRecordDto = JSONObject.parseObject(data, WaterRecordDto.class);
        return waterRecordDto;
    }


}
