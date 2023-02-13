package xyz.acproject.lang.exception;

import xyz.acproject.lang.enums.HttpCodeEnum;

import java.io.Serializable;

/**
 * @author Jane
 * @ClassName CustomException
 * @Description TODO
 * @date 2021/4/7 16:37
 * @Copyright:2021
 */
public class CustomException extends RuntimeException implements Serializable {
    private static final long serialVersionUID = 2501210900747384804L;
    private HttpCodeEnum codeEnum;

    public CustomException(HttpCodeEnum codeEnum) {
        super(codeEnum.getCn_msg());
        this.codeEnum = codeEnum;
    }

    public CustomException(){
        super("自定义错误返回");
        this.codeEnum = HttpCodeEnum.custom;
    }

    public HttpCodeEnum getCodeEnum() {
        return codeEnum;
    }
}
