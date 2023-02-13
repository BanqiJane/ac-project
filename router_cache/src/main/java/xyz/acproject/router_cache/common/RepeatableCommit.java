package xyz.acproject.router_cache.common;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import xyz.acproject.cache.RedisService;
import xyz.acproject.lang.enums.HttpCodeEnum;
import xyz.acproject.lang.response.Response;
import xyz.acproject.utils.random.RandomUtils;

import javax.annotation.Resource;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Jane
 * @ClassName RepeatableCommit
 * @Description TODO
 * @date 2021/1/14 15:26
 * @Copyright:2021
 */
@Component
public class RepeatableCommit {
    @Resource(name="redisServiceRouterSecurity")
    private RedisService redisService;
    private final static ReentrantLock lock1 = new ReentrantLock();

    private final static ReentrantLock lock2 = new ReentrantLock();
    private static final Logger LOGGER = LogManager.getLogger(RepeatableCommit.class);

    public Mono<Response> reCommit(long id, Class<?> clazz, int timeout){
        try {
            this.lock1.lock();
            String className = clazz.getName();
            String name = String.valueOf(id);
            String keyname = String.format("%s#%s", className, name);
            int hashCode = Math.abs(keyname.hashCode());
            String key = String.format("%s_%d", name, hashCode);
            if (timeout < 0) {
                //过期时间5秒
                timeout = 5;
            }
            String value = redisService.get(key);
            LOGGER.info("限制频繁提交:keyName:{},key:{},value:{},timeout:{}",keyname,key,value,timeout);
            if (StringUtils.isNotBlank(value)) {
                return Mono.just(new Response().custom(HttpCodeEnum.bussy));
            }
            redisService.set(key, RandomUtils.getUUID(), timeout);
        }catch (Exception e){

        }finally {
            this.lock1.unlock();
        }
        return null;
    }
    public Mono<Response> reCommit(String name, Class<?> clazz, int timeout){
        try {
            this.lock2.lock();
            String className = clazz.getName();
            String keyname = String.format("%s#%s", className, name);
            int hashCode = Math.abs(keyname.hashCode());
            String key = String.format("%s_%d", name, hashCode);
            if (timeout < 0) {
                //过期时间5秒
                timeout = 5;
            }
            String value = redisService.get(key);
            if (StringUtils.isNotBlank(value)) {
                return Mono.just(new Response().custom(HttpCodeEnum.bussy));
            }
            redisService.set(key, RandomUtils.getUUID(), timeout);
        }catch (Exception e){

        }finally {
            this.lock2.unlock();
        }
        return null;
    }
}
