package xyz.acproject.security_flux_demo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScan("xyz.acproject.security_flux_demo.dao")
@ComponentScan({"xyz.acproject"})
public class SecurityFluxDemoApplication {

    public static void main(String[] args) {
        System.setProperty("reactor.netty.http.server.accessLogEnabled", "true");
        SpringApplication.run(SecurityFluxDemoApplication.class, args);
    }

}
