package xyz.acproject.router_cache.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import xyz.acproject.cache.impl.RedisServiceImpl;

import javax.annotation.Resource;

/**
 * @author Jane
 * @ClassName CacheConfig
 * @Description TODO
 * @date 2021/4/7 23:59
 * @Copyright:2021
 */
@Configuration
@ComponentScan({"xyz.acproject"})
public class RouterCacheConfig {

    @Resource(name = "redisTemplateSecurity")
    private RedisTemplate redisTemplateSecurity;


    @Bean("redisServiceRouterSecurity")
    public RedisServiceImpl redisServiceRouterSecurity(){
        RedisServiceImpl redisService = new RedisServiceImpl();
        redisService.setRedisTemplate(redisTemplateSecurity);
        return redisService;
    }

}
