package xyz.acproject.datasource_mybatis.config;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * @author Jane
 * @ClassName DruidConfig
 * @Description TODO
 * @date 2022/6/2 15:51
 * @Copyright:2022
 */
@Configuration
public class DruidConfig {

    @Bean
    //这个加载配置不需要了，在DruidDataSourceWrapper早已存在
    //@ConfigurationProperties(prefix = "spring.datasource")
    public DataSource druid() {
        //return new DruidDataSource();
        //使用构造器模式创建数据源，这是druid里面提供给我们的入口
        return DruidDataSourceBuilder.create().build();
    }
}
