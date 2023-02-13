package xyz.acproject.lang.response;

import lombok.Getter;
import xyz.acproject.lang.enums.HttpCodeEnum;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Jane
 * @ClassName Response
 * @Description TODO
 * @date 2021/1/28 0:28
 * @Copyright:2021
 * @version 1.034 beta
 * @update last update by jane add T
 */
@Getter
public final class Response {

    /**
     * 状态码 理论上0之外都是异常状态码 参考地址 https://apidoc.touchdot.top/code
     */
    private Integer code;
    /**
     * 中文信息
     */
    private String message;
    /**
     * 英文信息
     */
    private String msg;
    /**
     * 请求时间戳
     */
    private Long timestamp;
    /**
    * 数据本体
    */
    private Object data;

    private static final ThreadLocal<HashMap<String, Object>> jsonMapContainer = ThreadLocal.withInitial(() -> new HashMap<>());

    public Response success(){
        this.code = 0;
        this.message = "请求成功";
        this.msg = "success";
        this.timestamp =System.currentTimeMillis();
        Map<String, Object> map = jsonMapContainer.get();
        jsonMapContainer.remove();
        this.data = map;
        return this;
    }

    public Response failure(String message) {
        this.code = -200;
        this.message = message;
        this.msg = "error";
        this.timestamp = System.currentTimeMillis();
        Map<String, Object> map = jsonMapContainer.get();
        jsonMapContainer.remove();
        this.data = map;
        return this;
    }
    public Response custom(HttpCodeEnum httpCodeEnum) {
        this.code = httpCodeEnum.getCode();
        this.message = httpCodeEnum.getCn_msg();
        this.msg = httpCodeEnum.getMsg();
        this.timestamp = System.currentTimeMillis();
        Map<String, Object> map = jsonMapContainer.get();
        jsonMapContainer.remove();
        this.data = map;
        return this;
    }
    public static void data(String key, Object value) {
        jsonMapContainer.get().put(key, value);
    }

    public Response data(Object data){
        this.data = data;
        return this;
    }

    public static void pageBean(Object page,Object list) {
        jsonMapContainer.get().put("list", list);
        jsonMapContainer.get().put("page", page);
    }
    public Response add(String key, Object value) {
        this.jsonMapContainer.get().put(key, value);
        return this;
    }

    public Response page(Object page,Object list) {
        this.jsonMapContainer.get().put("list", list);
        this.jsonMapContainer.get().put("page", page);
        return this;
    }
}
