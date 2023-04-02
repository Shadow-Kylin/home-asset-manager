package cn.shadowkylin.ham.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Data
@Configuration
@ConfigurationProperties(prefix = "jwt") //与配置文件中的数据关联起来(这个注解会自动匹配jwt开头的配置)
public class JwtProperties implements WebMvcConfigurer {
    /**
     * Request Headers ： Authorization
     */
    private String header;

    /**
     * Base64对该令牌进行编码
     */
    private String base64Secret;

    /**
     * 令牌过期时间 此处单位/毫秒
     */
    private Long tokenValidityInMillSeconds;
}