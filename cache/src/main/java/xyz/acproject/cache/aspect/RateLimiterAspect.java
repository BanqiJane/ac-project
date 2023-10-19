package xyz.acproject.cache.aspect;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RateIntervalUnit;
import org.redisson.api.RateType;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;
import xyz.acproject.cache.annotation.RateLimit;
import xyz.acproject.lang.exception.ServiceException;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author Admin
 * @ClassName RateLimiterAspect
 * @Description TODO
 * @date 2023/7/7 15:58
 * @Copyright:2023
 */
@Aspect
@Component
public class RateLimiterAspect {

//    @Resource
//    private RedissonClient redissonClient;
//
//
//
//    @Pointcut("execution(public * xyz.acproject.*.controller.*.*(..))")
//    public void pointcut() {
//    }
//
//    @Around("pointcut()")
//    public Object process(ProceedingJoinPoint point) throws Throwable {
//
//        MethodSignature signature = (MethodSignature) point.getSignature();
//
//        //反射看方法上有没有@RateLimit注解
//        RateLimit rateLimit = signature.getMethod().getAnnotation(RateLimit.class);
//        if (rateLimit == null) {
//            return point.proceed();
//        }
//        String key = rateLimit.key();
//        long count = rateLimit.count();
//        long time = rateLimit.time();
//
//        RRateLimiter rateLimiter = redissonClient.getRateLimiter(key);
//        rateLimiter.trySetRate(RateType.OVERALL,count, time, RateIntervalUnit.SECONDS);
//        if (!rateLimiter.tryAcquire()) {
//            throw new ServiceException("业务繁忙等会再来吧");
//        }
//        return point.proceed();
//    }
}