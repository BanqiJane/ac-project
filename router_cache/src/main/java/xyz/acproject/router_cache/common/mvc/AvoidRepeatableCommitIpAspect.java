package xyz.acproject.router_cache.common.mvc;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import xyz.acproject.cache.RedisService;
import xyz.acproject.lang.enums.HttpCodeEnum;
import xyz.acproject.lang.response.Response;
import xyz.acproject.router_cache.common.mvc.annotation.AvoidRepeatableCommitIp;
import xyz.acproject.utils.net.IpUtils;
import xyz.acproject.utils.random.RandomUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Jane
 * @ClassName AvoidRepeatableCommitAspect
 * @Description 防止请求重复提交
 * @date 2021/1/7 11:35
 * @Copyright:2021
 */
@Aspect
@Component
public class AvoidRepeatableCommitIpAspect {
    private transient Logger log = LoggerFactory.getLogger(AvoidRepeatableCommitIpAspect.class);
    @Resource(name="redisServiceRouterSecurity")
    private RedisService redisService;

    private final ReentrantLock lock = new ReentrantLock();

    @Around("@annotation(xyz.acproject.router_cache.common.mvc.annotation.AvoidRepeatableCommitIp)")
    public Object around(ProceedingJoinPoint point) throws Throwable {

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String ip = IpUtils.getIpAddr(request);
        try {
            this.lock.lock();
            //获取注解
            MethodSignature signature = (MethodSignature) point.getSignature();
            Method method = signature.getMethod();
            //目标类、方法
            String className = method.getDeclaringClass().getName();
            String name = method.getName();
            String ipKey = String.format("%s#%s", className, name);
            int hashCode = Math.abs(ipKey.hashCode());
            String key = String.format("%s_%d", ip, hashCode);
            log.debug("ipKey={},hashCode={},key={}", ipKey, hashCode, key);
            AvoidRepeatableCommitIp avoidRepeatableCommitIp = method.getAnnotation(AvoidRepeatableCommitIp.class);
            int timeout = avoidRepeatableCommitIp.timeout();
            if (timeout < 0) {
                //过期时间5秒
                timeout = 5;
            }
            String value = redisService.get(key);
            if (StringUtils.isNotBlank(value)) {
                return new Response().custom(HttpCodeEnum.bussy);
            }
            redisService.set(key, RandomUtils.getUUID(), timeout);
        }catch (Exception e){

        }finally {
            this.lock.unlock();
        }
        //执行方法
        Object object = point.proceed();
        return object;
    }
}
