package xyz.acproject.lang.exception;

import java.io.Serializable;

/**
 * @author Jane
 * @ClassName ServiceException
 * @Description TODO
 * @date 2021/1/30 16:15
 * @Copyright:2021
 */
public class ServiceException extends RuntimeException implements Serializable {
    private static final long serialVersionUID = -7904537313921457340L;

    private String message;

    public ServiceException(String message){
        super(message);
        this.message = message;
    }

    public ServiceException(){
        super("服务错误");
        this.message = "服务错误";
    }
}
