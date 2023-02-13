package xyz.acproject.lang.exception;

import java.io.Serializable;

/**
 * @author Jane
 * @ClassName TimeoutException
 * @Description TODO
 * @date 2021/8/11 10:46
 * @Copyright:2021
 */
public class TimeoutException extends RuntimeException implements Serializable {
    private static final long serialVersionUID = -8486393668049581504L;

    private String message;

    public TimeoutException(String message){
        super(message);
        this.message = message;
    }

    public TimeoutException(){
        super("超时错误");
        this.message = "超时错误";
    }
}
