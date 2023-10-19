package xyz.acproject.cache.config;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.deser.std.DateDeserializers;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.databind.ser.std.DateSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import xyz.acproject.cache.serializer.FastJsonRedisSerializer;
import xyz.acproject.utils.SpringUtils;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * @author Jane
 * @Description redis配置文件 基于springboot的RedisTemplate
 * @ClassName RedisConfig
 * @date 2021/1/23 21:51
 * @Copyright:2021 2.0
 */
@Configuration
@EnableCaching
//@AutoConfigureAfter(RedisAutoConfiguration.class)
public class RedisConfig extends CachingConfigurerSupport {
//	@Resource
//	private LettuceConnectionFactory lettuceConnectionFactory;

    private static final ThreadLocal<SimpleDateFormat> THREAD_LOCAL = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };

    //这里演示了如何自定义创建custom redis
//	@Bean("redisTemplateCustom")
//	public RedisTemplate<String,Serializable> redisTemplateCustom(RedisStandaloneConfiguration redisStandaloneConfiguration){
//		LettuceConnectionFactory connectionFactory =lettuceConnectionFactory(redisStandaloneConfiguration,lettucePoolingClientConfigurationGenericObjectPoolConfig());
//		connectionFactory.afterPropertiesSet();
//		return this.createRedisTemplate(connectionFactory);
//	}


    @Bean("redisTemplate")
    public RedisTemplate<String, Serializable> redisTemplate(LettuceConnectionFactory lettuceConnectionFactory) {
        return this.createRedisTemplate(lettuceConnectionFactory);
    }

    @Bean("redisTemplateSecurity")
    public RedisTemplate<String, Serializable> redisTemplateSecurity() {
        // jedisConnectionFactory(standaloneConfiguration, genericObjectPoolConfig, timeout);
        RedisFileSetConfig redisFileSetConfig = redisFileSetConfigSecurity();
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setPassword(redisFileSetConfig.getPassword());
        redisStandaloneConfiguration.setHostName(redisFileSetConfig.getHostName());
        redisStandaloneConfiguration.setPort(redisFileSetConfig.getPort());
        redisStandaloneConfiguration.setDatabase(redisFileSetConfig.getDatabase());
        redisStandaloneConfiguration.setUsername(redisFileSetConfig.getUsername());
        LettuceConnectionFactory connectionFactory = lettuceConnectionFactory(redisStandaloneConfiguration, lettucePoolingClientConfigurationGenericObjectPoolConfig());
        connectionFactory.afterPropertiesSet();
        return this.createRedisTemplate(connectionFactory);
    }




    @Bean("redisFileSetConfig")
    @ConfigurationProperties(prefix = "spring.redis-security")
    public RedisFileSetConfig redisFileSetConfigSecurity(){
        return new RedisFileSetConfig();
    }



    //这里演示了如何自定义这个配置类去配置redis自定义数据库
//    private RedisStandaloneConfiguration getRedisStandaloneConfiguration() {
//        RedisStandaloneConfiguration standaloneConfiguration = new RedisStandaloneConfiguration();
//        standaloneConfiguration.setDatabase(getDataBase_security());
//        standaloneConfiguration.setHostName(getHost_security());
//        standaloneConfiguration.setPassword(getPassword_security());
//        standaloneConfiguration.setPort(getPort_security());
//        return standaloneConfiguration;
//    }



    /**
     * lettuceConnectionFactory
     *
     * @param standaloneConfiguration Redis标准配置
     * @param genericObjectPoolConfig Redis通用配置
     *                                //	 * @param timeout                 超时时间
     * @return
     */
    private LettuceConnectionFactory lettuceConnectionFactory(RedisStandaloneConfiguration standaloneConfiguration, GenericObjectPoolConfig genericObjectPoolConfig) {
//		LettucePoolingClientConfiguration.LettucePoolingClientConfigurationBuilder builder = LettucePoolingClientConfiguration.builder();
//		builder.poolConfig(genericObjectPoolConfig);
        LettuceClientConfiguration clientConfiguration = LettucePoolingClientConfiguration.builder().poolConfig(genericObjectPoolConfig).build();
        return new LettuceConnectionFactory(standaloneConfiguration, clientConfiguration);
    }

    /**
     * 默认外界访问方法
     *
     * @param standaloneConfiguration 独立的配置
     * @param genericObjectPoolConfig 通用对象池配置
     * @return {@link LettuceConnectionFactory}
     */
    public synchronized LettuceConnectionFactory getLettuceConnectionFactory(RedisStandaloneConfiguration standaloneConfiguration, GenericObjectPoolConfig genericObjectPoolConfig) {
        return this.lettuceConnectionFactory(standaloneConfiguration, genericObjectPoolConfig);
    }

    public synchronized LettuceConnectionFactory getLettuceConnectionFactory(RedisStandaloneConfiguration standaloneConfiguration) {
        return this.lettuceConnectionFactory(standaloneConfiguration, this.lettucePoolingClientConfigurationGenericObjectPoolConfig());
    }

    //使用yml配置的默认连接池配置
    @Bean
    @ConfigurationProperties(prefix = "spring.redis.lettuce.pool")
    public GenericObjectPoolConfig<LettucePoolingClientConfiguration> lettucePoolingClientConfigurationGenericObjectPoolConfig() {
        return new GenericObjectPoolConfig<>();
    }


    /**
     * 默认外界访问方法
     *
     * @param lettuceConnectionFactory 生菜连接工厂
     * @return {@link RedisTemplate}<{@link String}, {@link Serializable}>
     */
    public synchronized RedisTemplate<String, Serializable> getCreateRedisTemplate(LettuceConnectionFactory lettuceConnectionFactory) {
        return this.createRedisTemplate(lettuceConnectionFactory);
    }


    private RedisTemplate<String, Serializable> createRedisTemplate(LettuceConnectionFactory lettuceConnectionFactory) {
        RedisTemplate<String, Serializable> redisTemplate = new RedisTemplate<>();
        //fastjson  待完善 可参考https://blog.csdn.net/qq_33396608/article/details/108277220
//        GenericFastJsonRedisSerializer fastJsonRedisSerializer = new GenericFastJsonRedisSerializer();
        //jackson
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.activateDefaultTyping(LaissezFaireSubTypeValidator.instance,
                ObjectMapper.DefaultTyping.NON_FINAL,
                JsonTypeInfo.As.WRAPPER_ARRAY);
        om.setDateFormat(THREAD_LOCAL.get());
        om.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        om.disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);
        JavaTimeModule timeModule = new JavaTimeModule();
        timeModule.addSerializer(Date.class, new DateSerializer(false, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")));
        timeModule.addDeserializer(Date.class, new DateDeserializers.DateDeserializer());
        timeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        timeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        timeModule.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        timeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        timeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern("HH:mm:ss")));
        timeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ofPattern("HH:mm:ss")));
        om.registerModule(timeModule);
//        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(om,Object.class);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.setHashKeySerializer(jackson2JsonRedisSerializer);
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.setConnectionFactory(lettuceConnectionFactory);
        //开启事务管理 需要注入数据源
//		redisTemplate.setEnableTransactionSupport(true);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    //创建事务级别的redis
    private RedisTemplate<String, Serializable> createTranRedisTemplate(LettuceConnectionFactory lettuceConnectionFactory) {
        RedisTemplate<String, Serializable> redisTemplate = new RedisTemplate<>();
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.activateDefaultTyping(LaissezFaireSubTypeValidator.instance,
                ObjectMapper.DefaultTyping.NON_FINAL,
                JsonTypeInfo.As.WRAPPER_ARRAY);
        JavaTimeModule timeModule = new JavaTimeModule();
        timeModule.addSerializer(Date.class, new DateSerializer(false, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")));
        timeModule.addDeserializer(Date.class, new DateDeserializers.DateDeserializer());
        timeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        timeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        timeModule.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        timeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        timeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern("HH:mm:ss")));
        timeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ofPattern("HH:mm:ss")));
        om.registerModule(timeModule);
//        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(om,Object.class);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.setHashKeySerializer(jackson2JsonRedisSerializer);
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.setConnectionFactory(lettuceConnectionFactory);
        //开启事务管理 需要注入数据源
        redisTemplate.setEnableTransactionSupport(true);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }


    @Bean
    public CacheManager cacheManager(LettuceConnectionFactory lettuceConnectionFactory) {

        //RedisCacheManager 生成器创建
        RedisCacheManager.RedisCacheManagerBuilder builder = RedisCacheManager.builder(RedisCacheWriter.lockingRedisCacheWriter(lettuceConnectionFactory)).cacheDefaults(this.cacheConfiguration())
                .withInitialCacheConfigurations(this.cacheConfigurationMap());
        RedisCacheManager cacheManager = builder.build();
        return cacheManager;
    }


    /*
     * jedis
     *
     * @param standaloneConfiguration Redis标准配置
     * @param genericObjectPoolConfig Redis通用配置
     * @param timeout                 超时时间
     * @return
     * */
//	private JedisConnectionFactory jedisConnectionFactory(RedisStandaloneConfiguration standaloneConfiguration, GenericObjectPoolConfig genericObjectPoolConfig, long timeout) {
//		JedisClientConfiguration.DefaultJedisClientConfigurationBuilder builder = (JedisClientConfiguration.DefaultJedisClientConfigurationBuilder) JedisClientConfiguration
//				.builder();
//		builder.connectTimeout(Duration.ofSeconds(timeout));
//		builder.usePooling();
//		builder.poolConfig(genericObjectPoolConfig);
//		return new JedisConnectionFactory(standaloneConfiguration, builder.build());
//	}


    /**
     * redis缓存管理器配置列表；
     * 可以根据业务需要配置不同的过期时间；
     */
    private Map<String, RedisCacheConfiguration> cacheConfigurationMap() {
        Map<String, RedisCacheConfiguration> configurationMap = new HashMap<>();
        configurationMap.put("fangulupu:user", this.cacheConfiguration());
        return configurationMap;
    }


    /**
     * redis缓存管理器的默认配置；
     * 使用fastJson序列化value,model不再需要实现Serializable接口；
     */
    private RedisCacheConfiguration cacheConfiguration() {
        //创建自定义序列化器
        FastJsonRedisSerializer jsonSeria = new FastJsonRedisSerializer();
        //包装成SerializationPair类型
        RedisSerializationContext.SerializationPair serializationPair = RedisSerializationContext.SerializationPair.fromSerializer(jsonSeria);
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig();
        //设置序列化器
        redisCacheConfiguration = redisCacheConfiguration.serializeValuesWith(serializationPair).entryTtl(Duration.ofSeconds(getSeconds()));
        return redisCacheConfiguration;
    }







    //	/**
//	 * 显示声明缓存key生成器
//	 *
//	 * @return
//	 */
//	@Bean
//	public KeyGenerator keyGenerator() {
//
//		return new SimpleKeyGenerator();
//	}

    /**
     * @return int
     * @Description 获取yml中的小时配置
     * @author Jane
     * @date 2021/1/24 13:34
     * @params []
     * @Copyright
     */
    public int getSeconds() {
        String key = SpringUtils.getConfigByKey("acproject.redis.expire.time");
        try {
            if (StringUtils.isNotBlank(key)) {
                return Integer.parseInt(key);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 86400;
    }

    public int getDataBase_security() {
        String key = SpringUtils.getConfigByKey("spring.redis-security.database");
        try {
            if (StringUtils.isNotBlank(key)) {
                return Integer.parseInt(key);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 15;
    }

    public String getPassword_security() {
        String key = SpringUtils.getConfigByKey("spring.redis-security.password");
        try {
            if (StringUtils.isBlank(key)) {
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return key;
    }

    public int getPort_security() {
        String key = SpringUtils.getConfigByKey("spring.redis-security.port");
        try {
            if (StringUtils.isNotBlank(key)) {
                return Integer.parseInt(key);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 6379;
    }

    public String getHost_security() {
        String key = SpringUtils.getConfigByKey("spring.redis-security.hostName");
        try {
            if (StringUtils.isNotBlank(key)) {
                return key;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "127.0.0.1";
    }

    public long getTimeOut_security() {
        String key = SpringUtils.getConfigByKey("spring.redis-security.timeout");
        try {
            if (StringUtils.isNotBlank(key)) {
                return Long.parseLong(key);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 30000;
    }

}
