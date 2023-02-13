package xyz.acproject.lang.exception;

import java.io.Serializable;

/**
 * @author Jane
 * @ClassName AuthException
 * @Description TODO
 * @date 2021/1/31 16:29
 * @Copyright:2021
 */
public class AuthException extends RuntimeException implements Serializable {
    private static final long serialVersionUID = -6780053007634536545L;

    private String message;

    public AuthException(String message){
        super(message);
        this.message = message;
    }
    public AuthException(){
        super("token验证失败");
        this.message = "token验证失败";
    }
}
