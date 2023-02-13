package xyz.acproject.lang.exception;

import lombok.Getter;

import java.io.Serializable;

/**
 * @author Jane
 * @ClassName ServerException
 * @Description TODO
 * @date 2021/1/28 23:44
 * @Copyright:2021
 */
@Getter
public class ServerException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = -49916816458128631L;

    private String message;

    public ServerException(String message){
        super(message);
        this.message = message;
    }

    public ServerException(){
        super("服务器错误");
        this.message = "服务器错误";
    }
}
