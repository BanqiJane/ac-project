package xyz.acproject.router_cache.common.mvc;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
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
@Component("repeatableCommitMvc")
public class RepeatableCommit {
    @Resource(name="redisServiceRouterSecurity")
    private RedisService redisService;
    private final ReentrantLock lock = new ReentrantLock();

    public Response reCommit(long id, Class<?> clazz, int timeout){
        try {
            this.lock.lock();
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
            if (StringUtils.isNotBlank(value)) {
                return new Response().custom(HttpCodeEnum.bussy);
            }
            redisService.set(key, RandomUtils.getUUID(), timeout);
        }catch (Exception e){

        }finally {
            this.lock.unlock();
        }
        return null;
    }
}
