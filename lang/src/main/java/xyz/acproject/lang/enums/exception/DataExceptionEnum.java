package xyz.acproject.lang.enums.exception;

/**
 * @author Jane
 * @ClassName DataExceptionEnum
 * @Description TODO
 * @date 2021/1/28 23:53
 * @Copyright:2021
 */
public enum DataExceptionEnum implements BaseExceptionEnum {
    ERROR(1,"error","错误")
    ;


    private final Integer code;

    private final String msg;

    private final String message;

    DataExceptionEnum(Integer code, String msg, String message) {
        this.code = code;
        this.msg = msg;
        this.message = message;
    }

    @Override
    public Integer getCode() {
        return this.code;
    }

    @Override
    public String getMsg() {
        return this.msg;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
