package cn.shadowkylin.ham.config;

import cn.shadowkylin.ham.common.TokenInterceptor;
import org.apache.http.HttpRequestInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @创建人 li cong
 * @创建时间 2023/3/31
 * @描述
 */

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private TokenInterceptor tokenInterceptor;
    /**
     * 请求拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //注册拦截器
        InterceptorRegistration registration = registry.addInterceptor(tokenInterceptor);
        //拦截路径
        registration.addPathPatterns("/**").excludePathPatterns("/auth/login","/auth/register","/auth/sendSMS");
    }
}
