package xyz.acproject.security_demo.config;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Admin
 * @ClassName RedissonConfig
 * @Description TODO
 * @date 2023/7/7 15:27
 * @Copyright:2023
 */
@Configuration
@RequiredArgsConstructor
public class RedissonConfig {

    private final RedisProperties redisProperties;

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        //单节点配置
        String address = "redis://" + redisProperties.getHost() + ":" + redisProperties.getPort();
        SingleServerConfig serverConfig = config.useSingleServer();
        serverConfig.setAddress(address);
        if (!StringUtils.isEmpty(redisProperties.getPassword())) {
            serverConfig.setPassword(redisProperties.getPassword());
        }
        serverConfig.setDatabase(redisProperties.getDatabase());

        //看门狗的锁续期时间，默认30000ms，这里配置成15000ms
        config.setLockWatchdogTimeout(15000);
        return Redisson.create(config);
    }
}
