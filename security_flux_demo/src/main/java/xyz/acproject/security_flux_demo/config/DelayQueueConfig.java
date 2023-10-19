package xyz.acproject.security_flux_demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import xyz.acproject.security_flux_demo.dtos.DanMu;

import java.util.concurrent.DelayQueue;

/**
 * @author Admin
 * @ClassName DelayQueueConfig
 * @Description TODO
 * @date 2023/1/13 10:36
 * @Copyright:2023
 */
@Configuration
public class DelayQueueConfig {
    @Bean("danMuDelayQueue")
    public DelayQueue<DanMu> danMuDelayQueue(){
        return new DelayQueue<DanMu>();
    }

}
