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
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @author Jane
 * @ClassName ReactiveRedisConfig
 * @Description TODO
 * @date 2022/6/20 16:52
 * @Copyright:2022
 */
@Configuration
@EnableCaching
public class ReactiveRedisConfig {

    private static final ThreadLocal<SimpleDateFormat> THREAD_LOCAL = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };
    @Bean("redisSerializationContext")
    public RedisSerializationContext<String, Serializable> redisSerializationContext() {
        RedisSerializationContext.RedisSerializationContextBuilder<String, Serializable> builder =
                RedisSerializationContext.newSerializationContext();
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
        builder.key(stringRedisSerializer);
        builder.value(jackson2JsonRedisSerializer);
        builder.hashKey(jackson2JsonRedisSerializer);
        builder.hashValue(jackson2JsonRedisSerializer);
        //开启事务管理 需要注入数据源
//		redisTemplate.setEnableTransactionSupport(true);
        return builder.build();
    }

    @Bean("reactiveRedisTemplate")
    public ReactiveRedisTemplate<String, Serializable> reactiveRedisTemplate(LettuceConnectionFactory connectionFactory) {
        return new ReactiveRedisTemplate<>(connectionFactory, redisSerializationContext());
    }
}
