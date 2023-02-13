package xyz.acproject.datasource_mybatis.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * querywrapper分页
 * @author Jane
 * @ClassName MybatisPlusConfig
 * @Description TODO
 * @date 2021/4/16 10:14
 * @Copyright:2021
 */
@Configuration
public class MybatisPlusConfig {
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor(){
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        interceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());
        return interceptor;
    }

    //3.4.3 remove
//    @Bean
//    public ConfigurationCustomizer configurationCustomizer(){
//        return  configuration -> configuration.setUseDeprecatedExecutor(false);
//    }
}
