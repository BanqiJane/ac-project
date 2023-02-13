package xyz.acproject.swagger.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import xyz.acproject.utils.SpringUtils;

/**
 * @author Jane
 * @ClassName SwaggerConfig
 * @Description TODO
 * @date 2021/2/26 9:47
 * @Copyright:2021
 */
@Configuration
@EnableOpenApi
public class SwaggerConfig {
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .pathMapping("/")
                .select()
                .apis(RequestHandlerSelectors.basePackage(getBasePackage()))
                .paths(PathSelectors.any())
                .build().apiInfo(new ApiInfoBuilder()
                        .title(getTitle())
                        .description(getIntro())
                        .version(getVersion())
                        .contact(new Contact(getCName(),getCNet(),getCEmail()))
                        .license("The Apache GPL V3.0 License")
                        .licenseUrl("doc.acproject.xyz")
                        .build()).enable(getEnable());
    }


    private Boolean getEnable() {
        String key = SpringUtils.getConfigByKey("acproject.swagger.enable");
        try {
            if (key != null) {
                return Boolean.parseBoolean(key);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    private String getBasePackage(){
        String key = SpringUtils.getConfigByKey("acproject.swagger.package");
        try {
            if (key != null) {
                return key;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return "xyz.acproject";
    }
    private String getTitle(){
        String key = SpringUtils.getConfigByKey("acproject.swagger.title");
        try {
            if (key != null) {
                return key;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return "fangulupu doc";
    }

    private String getIntro(){
        String key = SpringUtils.getConfigByKey("acproject.swagger.descr");
        try {
            if (key != null) {
                return key;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return "fangulupu doc descr";
    }
    private String getVersion(){
        String key = SpringUtils.getConfigByKey("acproject.swagger.version");
        try {
            if (key != null) {
                return key;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return "1.0";
    }

    private String getCName(){
        String key = SpringUtils.getConfigByKey("acproject.swagger.contact.name");
        try {
            if (key != null) {
                return key;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return "fangulupu";
    }
    private String getCNet(){
        String key = SpringUtils.getConfigByKey("acproject.swagger.contact.network");
        try {
            if (key != null) {
                return key;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return "acproject.xyz";
    }
    private String getCEmail(){
        String key = SpringUtils.getConfigByKey("acproject.swagger.contact.email");
        try {
            if (key != null) {
                return key;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return "zhangsheng@acproject.xyz";
    }
}