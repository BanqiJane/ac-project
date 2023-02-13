package xyz.acproject.lang.exception;

import java.io.Serializable;

/**
 * @author Jane
 * @ClassName SystemForbidden
 * @Description TODO
 * @date 2021/3/5 0:55
 * @Copyright:2021
 */
public class SystemForbiddenException extends RuntimeException implements Serializable {
    private static final long serialVersionUID = 7190629348160422267L;

    private String message;

    public SystemForbiddenException(String message){
        super(message);
        this.message = message;
    }
    public SystemForbiddenException(){
        super("系统禁止访问");
        this.message = "系统禁止访问";
    }
}
