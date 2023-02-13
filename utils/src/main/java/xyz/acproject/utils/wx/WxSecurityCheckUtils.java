package xyz.acproject.utils.wx;

import com.alibaba.fastjson.JSONObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xyz.acproject.lang.enums.HttpCodeEnum;
import xyz.acproject.lang.exception.CustomException;
import xyz.acproject.utils.http.OkHttp3Utils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Jane
 * @ClassName WxSecurityCheck
 * @Description TODO
 * @date 2021/3/3 23:08
 * @Copyright:2021
 */
public class WxSecurityCheckUtils {
    private static Logger LOGGER = LogManager.getLogger(WxSecurityCheckUtils.class);
    public static void checkImage(String accessToken, String contentType,byte[] bytes){
        String data = null;
        Map<String, String> headers = null;
        try {
            data = OkHttp3Utils.getHttp3Utils().httpPostFileForm("https://api.weixin.qq.com/wxa/img_sec_check?access_token=" + accessToken,headers,bytes)
                    .body().string();
        } catch (Exception e) {
            // TODO 自动生成的 catch 块
            LOGGER.error(e);
            data = null;
        }
        checkResult(data);
    }
    public static void checkResult(String result) {
        JSONObject jsonObject = JSONObject.parseObject(result);
        int errCode = (int) jsonObject.get("errcode");
        LOGGER.debug(jsonObject.toJSONString());
        if (errCode != 0) {
            throw new CustomException(HttpCodeEnum.wxsecuritycheckerror);
        }
    }
    public static void checkMsg(String accessToken,String msg){
        String data = null;
        Map<String, String> headers = null;
        Map<String, String> params = null;
        params = new HashMap<>(2);
        params.put("content",msg);

        try {
            data = OkHttp3Utils.getHttp3Utils().httpPostJson("https://api.weixin.qq.com/wxa/msg_sec_check?access_token=" + accessToken,headers,JSONObject.toJSONString(params))
                    .body().string();
        } catch (Exception e) {
            // TODO 自动生成的 catch 块
            LOGGER.error(e);
            data = null;
        }
        checkResult(data);
    }
}
