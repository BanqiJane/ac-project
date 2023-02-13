package xyz.acproject.security_flux_demo.config;

import org.springframework.context.annotation.Bean;
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
public class CacheConfig {
    @Resource(name = "redisTemplate")
    private RedisTemplate redisTemplate;
    @Resource(name = "redisTemplateSecurity")
    private RedisTemplate redisTemplateSecurity;

    @Bean("redisService")
    public RedisServiceImpl redisService(){
        RedisServiceImpl redisService = new RedisServiceImpl();
        redisService.setRedisTemplate(redisTemplate);
        return redisService;
    }

    @Bean("redisServiceSecurity")
    public RedisServiceImpl redisServiceSecurity(){
        RedisServiceImpl redisService = new RedisServiceImpl();
        redisService.setRedisTemplate(redisTemplateSecurity);
        return redisService;
    }

}
