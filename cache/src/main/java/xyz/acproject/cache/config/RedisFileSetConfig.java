package xyz.acproject.cache.config;

import lombok.Data;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.lang.Nullable;

/**
 * @author Jane
 * @ClassName RedisFileSetConfig
 * @Description TODO
 * @date 2022/6/27 11:06
 * @Copyright:2022
 */
@Data
public class RedisFileSetConfig {
    private static final String DEFAULT_HOST = "127.0.0.1";
    private static final int DEFAULT_PORT = 6379;

    private String hostName = DEFAULT_HOST;
    private int port = DEFAULT_PORT;
    private int database = 15;
    private String username = null;
    private RedisPassword password = RedisPassword.none();
}
