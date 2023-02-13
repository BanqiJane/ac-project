package xyz.acproject.router_cache.common.mvc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Jane
 * @ClassName AvoidRepeatableCommitMac
 * @Description TODO
 * @date 2021/3/16 14:08
 * @Copyright:2021
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AvoidRepeatableCommitMac {
    int timeout() default 30;
}
