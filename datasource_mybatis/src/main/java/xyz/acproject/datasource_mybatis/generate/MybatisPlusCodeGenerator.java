package xyz.acproject.datasource_mybatis.generate;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import xyz.acproject.datasource_mybatis.dao.BaseDao;
import xyz.acproject.datasource_mybatis.entity.BaseEntity;
import xyz.acproject.datasource_mybatis.service.BaseService;
import xyz.acproject.datasource_mybatis.service.impl.BaseServiceImpl;

import java.util.Collections;

//执行 main 方法，控制台输入模块表名，回车自动生成对应项目目录中
public class MybatisPlusCodeGenerator {

    private static final String URL = "jdbc:mysql://localhost:3306/security_flux_demo?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8";
    // 数据库用户名
    private static final String USERNAME = "root";
    // 数据库密码
    private static final String PASSWORD = "root";

    private static final String projectRootPath = System.getProperty("user.dir");

    private static final String parentPackageName = "xyz.acproject.security_flux_demo";

    public static void main(String[] args) {
        //目标模块
        String model = "/base-data";
        //路径
        String packagePath = projectRootPath + "/" + model + "/java";
        // XML文件的路径
        String mapperXmlPath = projectRootPath + "/" + model + "/mapper";
        //数据库配置
        DataSourceConfig.Builder dataSourceConfigBuilder = new DataSourceConfig.Builder(URL, USERNAME, PASSWORD);
        // 开始执行代码生成
        FastAutoGenerator.create(dataSourceConfigBuilder)
                // 1. 全局配置
                .globalConfig(builder -> builder
                        // 作者名称
                        .author("jane")
                        // 开启覆盖已生成的文件。注释掉则关闭覆盖。
                        .fileOverride()
                        // 禁止打开输出目录。注释掉则生成完毕后，自动打开生成的文件目录。
                        .disableOpenDir()
                        // 指定输出目录。如果指定，Windows生成至D盘根目录下，Linux or MAC 生成至 /tmp 目录下。
                        .outputDir(packagePath)
                        // 指定时间策略。
                        .dateType(DateType.ONLY_DATE)
                        // 注释时间策略。
                        .commentDate("yyyy-MM-dd")
                )

                // 2. 包配置
                .packageConfig((scanner, builder) -> builder
                                // 设置父表名
                                .parent(parentPackageName)
                                // mapper.xml 文件的路径。单模块下，其他文件路径默认即可。
                                .pathInfo(Collections.singletonMap(OutputFile.xml, mapperXmlPath))
                                .entity("entity")
                                .service("service")
                                .serviceImpl("service.impl")
                                .mapper("dao")
//                                .xml("mapper.xml")
                                .controller("controller")
                )

                // 3. 策略配置
                .strategyConfig((scanner, builder) -> builder.addInclude()

                        // --1>：Entity实体类策略配置
                        .entityBuilder()
                        //开启 lombok 模型
                        .enableLombok()
                        .disableSerialVersionUID()
                        .enableChainModel()
                        .addIgnoreColumns("id", "enable", "create_time", "update_time", "remark")
                        .superClass(BaseEntity.class)

                        // --2>：Mapper策略配置
                        .mapperBuilder()
                        .formatMapperFileName("%sDao")
                        .superClass(BaseDao.class)
                        .formatXmlFileName("%sMapper")


                        // --3>：service策略配置
                        .serviceBuilder()
                        .formatServiceFileName("%sService")
                        .formatServiceImplFileName("%sServiceImpl")
                        .superServiceClass(BaseService.class)
                        .superServiceImplClass(BaseServiceImpl.class)


                        // --4>：Controller策略配置
                        .controllerBuilder()
                        // 会在控制类中加[@RestController]注解。
                        .enableRestStyle()
                        // 开启驼峰转连字符
                        .enableHyphenStyle()
                        .formatFileName("%sController")
//                        .superClass(Controller.class)
                        .build()
                )
                //自定义配置
                .injectionConfig(builder -> {
                    builder.beforeOutputFile((tableInfo, objectMap) -> {
                        System.out.println("tableInfo: " + tableInfo.toString() + " objectMap: " + objectMap.toString());
                    });
                })
                // 4. 模板引擎配置，默认 Velocity 可选模板引擎 Beetl 或 Freemarker
                //.templateEngine(new BeetlTemplateEngine())
                .templateEngine(new FreemarkerTemplateEngine())

                // 5. 执行
                .execute();
    }

}
