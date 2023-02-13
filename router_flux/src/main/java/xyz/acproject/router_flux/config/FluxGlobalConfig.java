package xyz.acproject.router_flux.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.deser.std.DateDeserializers;
import com.fasterxml.jackson.databind.ser.std.DateSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.http.codec.multipart.DefaultPartHttpMessageReader;
import org.springframework.http.codec.multipart.MultipartHttpMessageReader;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.ResourceHandlerRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.result.view.ViewResolver;
import xyz.acproject.router_flux.advice.GlobalErrorWebExceptionHandler;
import xyz.acproject.utils.SpringUtils;
import xyz.acproject.utils.io.JarFileUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * @author Jane
 * @ClassName FluxCorsConfig
 * @Description TODO
 * @date 2021/3/27 21:11
 * @Copyright:2021
 */
@Configuration
@EnableWebFlux
public class FluxGlobalConfig implements WebFluxConfigurer {




    private static final ThreadLocal<SimpleDateFormat> THREAD_LOCAL = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };



    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        boolean enable = getStaticEnable();
        if(enable) {
            String type = getStaticType();
            if(type.equals("CUSTOM")){
                registry.addResourceHandler(getStaticPrefix()).addResourceLocations(getStaticPaths());
            }else{
                registry.addResourceHandler("/file/**").addResourceLocations(JarFileUtils.getBaseJarPath() + "/file/");
            }
        }
    }


    //2.6.0
    @Bean("webProperties")
    public WebProperties.Resources webProperties(){
        return new WebProperties().getResources();
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowCredentials(true)
                .allowedMethods("GET", "POST", "DELETE", "PUT", "PATCH")
                .maxAge(3600);
    }
//    @Override
//    public void addFormatters(FormatterRegistry registry) {
//        DateTimeFormatterRegistrar registrar = new DateTimeFormatterRegistrar();
//        registrar.setDateTimeFormatter(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
//        registrar.setTimeFormatter(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
//        registrar.setDateFormatter(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
//        registrar.setUseIsoFormat(true);
//        registrar.registerFormatters(registry);
//    }

//    @Bean
//    public Module dateTimeModule(){
//        JavaTimeModule timeModule = new JavaTimeModule();
//        timeModule.addSerializer(Date.class, new DateSerializer(Boolean.FALSE, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")));
//        timeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
//        timeModule.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
//        timeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern("HH:mm:ss")));
//        timeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
//        timeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
//        timeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ofPattern("HH:mm:ss")));
//        return timeModule;
//    }

    //    @Bean
//    public Jackson2ObjectMapperBuilderCustomizer
//    jacksonObjectMapperBuilderCustomizer() {
//        return new Jackson2ObjectMapperBuilderCustomizer() {
//
//            @Override
//            public void customize(
//                    Jackson2ObjectMapperBuilder jacksonObjectMapperBuilder) {
//                final String dateFormat = "yyyy-MM-dd";
//                final String timeFormat = "HH:mm:ss";
//                final String dateTimeFormat = "yyyy-MM-dd HH:mm:ss";
//                jacksonObjectMapperBuilder
//                        .serializers(
//                                new LocalDateSerializer(
//                                        DateTimeFormatter.ofPattern(dateFormat)))
//                        .deserializers(
//                                new LocalDateDeserializer(
//                                        DateTimeFormatter.ofPattern(dateFormat)))
//                        .serializers(
//                                new LocalTimeSerializer(
//                                        DateTimeFormatter.ofPattern(timeFormat)))
//                        .deserializers(
//                                new LocalTimeDeserializer(
//                                        DateTimeFormatter.ofPattern(timeFormat)))
//                        .serializers(
//                                new LocalDateTimeSerializer(
//                                        DateTimeFormatter.ofPattern(dateTimeFormat)))
//                        .deserializers(
//                                new LocalDateTimeDeserializer(
//                                        DateTimeFormatter.ofPattern(dateTimeFormat)));
//            }
//        };
//    }
    @Bean
    @Primary
    public ObjectMapper mapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setDateFormat(THREAD_LOCAL.get());
//        mapper.setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL);
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);
        mapper.enable(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN);
        JavaTimeModule timeModule = new JavaTimeModule();
        timeModule.addSerializer(Date.class, new DateSerializer(false, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")));
        timeModule.addDeserializer(Date.class, new DateDeserializers.DateDeserializer());
        timeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        timeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        timeModule.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        timeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        timeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern("HH:mm:ss")));
        timeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ofPattern("HH:mm:ss")));
        if(getMissField()) {
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        }
        mapper.registerModule(timeModule);

//        mapper.registerModule(timeModule).registerModule(new ParameterNamesModule()).registerModules(ObjectMapper.findModules());
        return mapper;
    }

    @Override
    public void configureHttpMessageCodecs(ServerCodecConfigurer configurer) {
        ObjectMapper mapper = mapper();
        configurer.defaultCodecs().jackson2JsonEncoder(new Jackson2JsonEncoder(mapper));
        configurer.defaultCodecs().jackson2JsonDecoder(new Jackson2JsonDecoder(mapper));
        //硬盘的
        DefaultPartHttpMessageReader partReader = new DefaultPartHttpMessageReader();
        partReader.setMaxParts(this.getMaxNum());
        partReader.setMaxDiskUsagePerPart(this.getMaxSize());
//        partReader.setMaxHeadersSize(921600);
        partReader.setEnableLoggingRequestDetails(true);
//        partReader.setStreaming(true);

        MultipartHttpMessageReader multipartReader = new MultipartHttpMessageReader(partReader);
        multipartReader.setEnableLoggingRequestDetails(true);

        configurer.defaultCodecs().multipartReader(multipartReader);
    }

    //异常处理
    @Bean
    @Order(-2)
    public ErrorWebExceptionHandler errorWebExceptionHandler(ErrorAttributes errorAttributes,
                                                             WebProperties.Resources webProperties, ObjectProvider<ViewResolver> viewResolvers,
                                                             ServerCodecConfigurer serverCodecConfigurer, ApplicationContext applicationContext) {
        GlobalErrorWebExceptionHandler exceptionHandler = new GlobalErrorWebExceptionHandler(errorAttributes,
                webProperties, applicationContext);
        //必须手动设置 下面三项配置 特别是messageWriters
        exceptionHandler.setViewResolvers(viewResolvers.orderedStream().collect(Collectors.toList()));
        exceptionHandler.setMessageWriters(serverCodecConfigurer.getWriters());
        exceptionHandler.setMessageReaders(serverCodecConfigurer.getReaders());
        return exceptionHandler;
    }



    private static String getStaticType() {
        String key = SpringUtils.getConfigByKey("acproject.webflux.static.type");
        if (StringUtils.isBlank(key)) {
            return "";
        }
        return key;
    }

    private static boolean getStaticEnable() {
        String key = SpringUtils.getConfigByKey("acproject.webflux.static.enable");
        if (StringUtils.isBlank(key)) {
            return false;
        }
        return Boolean.parseBoolean(key);
    }

    private static String getStaticPrefix(){
        String key = SpringUtils.getConfigByKey("acproject.webflux.static.prefix");
        if (StringUtils.isBlank(key)) {
            return "/**";
        }
        return key;
    }

    private static String[] getStaticPaths(){
        String key = SpringUtils.getConfigByKey("acproject.webflux.static.paths");
        if (StringUtils.isBlank(key)) {
            return new String[]{};
        }
        return key.split(",");
    }


    private static boolean getMissField() {
        String key = SpringUtils.getConfigByKey("acproject.webflux.miss-field");
        if (StringUtils.isBlank(key)) {
            return false;
        }
        return Boolean.parseBoolean(key);
    }


    private static int getMaxNum() {
        String key = SpringUtils.getConfigByKey("acproject.webflux.max-file-num");
        if (StringUtils.isBlank(key)) {
            return -1;
        }
        return Integer.parseInt(key);
    }


    private static Long getMaxSize() {
        String key = SpringUtils.getConfigByKey("acproject.webflux.max-file-size");
        if (StringUtils.isBlank(key)) {
            return -1L;
        }
        if (key.endsWith("MB") || key.endsWith("mb")) {
            key = key.substring(0, key.indexOf("MB"));
        }
        if (key.endsWith("mb")) {
            key.substring(0, key.indexOf("mb"));
        }

        return new BigDecimal(key).multiply(new BigDecimal(1000L * 1024L)).longValue();
    }

//    @Bean
//    CorsWebFilter corsFilter() {
//        CorsConfiguration config = new CorsConfiguration();
//        // Possibly...
//        // config.applyPermitDefaultValues()
//        config.setAllowCredentials(true);
//        config.addAllowedOrigin("*");//这里自定义域名
//        config.addAllowedHeader("*");
//        config.addAllowedMethod("*");
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", config);
//        return new CorsWebFilter(source);
//    }
}
