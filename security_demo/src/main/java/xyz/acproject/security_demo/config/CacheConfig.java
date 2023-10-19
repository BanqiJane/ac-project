package xyz.acproject.security_demo.config;

import com.gdcx.GdcxClient;
import com.gdcx.GdcxClientBuilder;
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



    @Bean
    public GdcxClient openApiClient() {
        //https://sms-api.cx-online.net/
        return new GdcxClientBuilder("https://sms-api.cx-online.net/","1688451780012253184",
                "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBANVRz9PJIBY4K-rTDbGJM-Op4dlk_vC5SDFzy_3UkEdHIf09c8csnLBtV1cBdEP6Dc5UCHBt2xyRlbPKrsPWGPcz8UnsHBug_t73ianjgeFLyhTicduRuc8HNeKaULwk41odE732eqOg1VX3YT0UnNZ0AO1nVQRt1KHQ6mDKU27hAgMBAAECgYEAkfLalZUq_ejKq2xhFxG_TcmCe4GtJ7gs26DVdcKEQDA74g60cxZj0hXNRvr4uDWSUpON_pY67q3w1cUbk2OdSewPW91cOVwDG5wfAxLmbhkoHlw28uBkmjtur_95x6drl54QCHiZ8DxH70kWGpZfDS3ZNpUf1or3m3zVQdpoYZ0CQQD9XmYHTXm0aUU7TztChz6_RpyQFekdycDuw5v0effQ-U5GzDGq8KxbiVpm-Jswq6NpIpr5pM53WIMSaZzI-S0XAkEA14jwdipzilMLIeP2pHEUls_aG-VwNE3-GQjvx5aE7LkAxMVg1_OVXbhkxQAurHeCBR3uxrGz7EC9mP6GyrruxwJADiZTdFzzSwwF_cXy_UgoTO5YmANhPXSi_bKW5xRYrqb9pB6LN4yxU3tcPRvtLs6nFZwFI6P8k9WxnDmhs-eWiQJAN7pXeiYVw7EfEr8veWsniKrrWQUk0tFLnst5u-BGNDLNaRMjrBJ2D0pq-6jFaKBpxMrtfwBZZf-DRjihP2-_5wJAFmXPeCWqiiKCopcd55DGYfRIYPmWqrQ0h6s_uTn-TmLWmqNj3c3wp43upIUBiEomyKfAuFNkB5QELeux48ebtA")
                .build();
    }
}
