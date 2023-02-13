package xyz.acproject.utils.config;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Jane
 * @ClassName YmlConfig
 * @Description TODO
 * @date 2021/1/23 21:51
 * @Copyright:2021
 */
@Component
@ConfigurationProperties(prefix = "acproject")
@Data
@Getter
@Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
public class YmlConfig {
    private Map<String,String> mapProps = new HashMap<>();
}
