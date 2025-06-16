package org.example.common.config;

import lombok.Data;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 项目主要配置<br>
 * 将application.yaml中的配置项映射到此实体类
 */
@Data
@Component
@ConfigurationProperties(prefix = "rain")
public class RainConfig {
    /**
     * 项目名称
     */
    private String name;

    /**
     * 验证码类型
     */
    @Getter
    private static String captchaType;

}
