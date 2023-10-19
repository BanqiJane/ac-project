package xyz.acproject.security_demo.param;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author Jane
 * @ClassName LoginParam
 * @Description TODO
 * @date 2021/4/17 23:44
 * @Copyright:2021
 */
@Data
public class LoginParam {
    @NotNull
    private String account;
    @NotNull
    private String password;
}
