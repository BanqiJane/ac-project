package xyz.acproject.swagger.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;
import xyz.acproject.router.config.CorsConfig;

/**
 * @author Jane
 * @ClassName WebMvcStaticConfig
 * @Description TODO
 * @date 2021/2/26 10:37
 * @Copyright:2021
 */
@Configuration
public class WebMvcStaticConfig extends CorsConfig {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("doc.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
        super.addResourceHandlers(registry);
    }
}