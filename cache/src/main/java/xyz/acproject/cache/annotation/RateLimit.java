package xyz.acproject.cache.annotation;

import java.lang.annotation.*;

/**
 * @author Admin
 * @ClassName RateLimit
 * @Description TODO
 * @date 2023/7/7 15:56
 * @Copyright:2023
 */


@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface RateLimit {


    String key() default "";


    /**
     * 单位时间内限制的访问次数
     *
     * @return
     */
    int count();
    /**
     * 限流单位时间（单位为秒）
     *
     * @return
     */
    int time() default 1;



//    //一次取多少个令牌
//    long permits();
//
//    //获取令牌的超时时间，单位为毫秒
//    long timeout() default 0;
}