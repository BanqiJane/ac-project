package xyz.acproject.router_cache.common.mvc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Jane
 * @ClassName AvoidRepeatableCommit
 * @Description 防止请求重复提交 注意使用该注解 使用包的springmvc配置文件必须添加aop动态代理配置  <aop:aspectj-autoproxy proxy-target-class="true"/>
 * @date 2021/1/7 11:33
 * @Copyright:2021
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AvoidRepeatableCommitIp {
    int timeout() default 30;
}
