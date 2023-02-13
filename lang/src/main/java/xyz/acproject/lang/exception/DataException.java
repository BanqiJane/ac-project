package xyz.acproject.lang.exception;

import lombok.Getter;

import java.io.Serializable;

/**
 * @author Jane
 * @ClassName DataException
 * @Description TODO
 * @date 2021/1/27 16:22
 * @Copyright:2021
 */
@Getter
public class DataException extends RuntimeException implements Serializable {
    private static final long serialVersionUID = 3686058611962773160L;

    private String message;


    public DataException(String message) {
        super(message);
        this.message = message;
    }

    public DataException() {
        super("数据错误");
        this.message = "数据错误";
    }
}
