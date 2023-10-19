package xyz.acproject.lang.response;

import lombok.Getter;
import xyz.acproject.lang.enums.HttpCodeEnum;
import xyz.acproject.lang.page.PageBean;

import java.util.HashMap;
import java.util.LinkedHashMap;
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
public final class Response<T> {

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
    private T data;

    private static final ThreadLocal<LinkedHashMap<String, Object>> jsonMapContainer
            = ThreadLocal.withInitial(() -> new LinkedHashMap<>());

    public Response success(){
        this.code = 0;
        this.message = "请求成功";
        this.msg = "success";
        this.timestamp =System.currentTimeMillis();
        Map<String, Object> map = jsonMapContainer.get();
        jsonMapContainer.remove();
        this.data = (T) map;
        return this;
    }

    public Response failure(String message) {
        this.code = -200;
        this.message = message;
        this.msg = "error";
        this.timestamp = System.currentTimeMillis();
        Map<String, Object> map = jsonMapContainer.get();
        jsonMapContainer.remove();
        this.data = (T) map;
        return this;
    }
    public Response custom(HttpCodeEnum httpCodeEnum) {
        this.code = httpCodeEnum.getCode();
        this.message = httpCodeEnum.getCn_msg();
        this.msg = httpCodeEnum.getMsg();
        this.timestamp = System.currentTimeMillis();
        Map<String, Object> map = jsonMapContainer.get();
        jsonMapContainer.remove();
        this.data = (T) map;
        return this;
    }

    public static void data(String key, Object value) {
        jsonMapContainer.get().put(key, value);
    }

    public Response data(T data){
        this.data = data;
        return this;
    }

    public static void pageBean(Object page,Object list) {
        jsonMapContainer.get().put("page", page);
        jsonMapContainer.get().put("list", list);
    }
    public static void pageBean(PageBean<?> pageBean) {
        jsonMapContainer.get().put("page", pageBean.getPage());
        jsonMapContainer.get().put("list", pageBean.getList());
    }
    public Response add(String key, Object value) {
        this.jsonMapContainer.get().put(key, value);
        if(this.data!=null){
            Map<String, Object> map = jsonMapContainer.get();
            jsonMapContainer.remove();
            if(this.data instanceof HashMap){
                ((Map<String, Object>)this.data).putAll(map);
            }else{
                this.data = (T) map;
            }
        }
        return this;
    }

    public Response page(Object page,Object list) {
        this.jsonMapContainer.get().put("page", page);
        this.jsonMapContainer.get().put("list", list);
        if(this.data!=null){
            Map<String, Object> map = jsonMapContainer.get();
            jsonMapContainer.remove();
            if(this.data instanceof HashMap){
                ((Map<String, Object>)this.data).putAll(map);
            }else{
                this.data = (T) map;
            }
        }
        return this;
    }
}
