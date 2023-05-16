package cn.shadowkylin.ham.config;

import cn.shadowkylin.ham.common.TokenInterceptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.resource.PathResourceResolver;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

/**
 * @创建人 li cong
 * @创建时间 2023/3/31
 * @描述
 */

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Resource
    private TokenInterceptor tokenInterceptor;

    /**
     * 请求拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //注册拦截器
        InterceptorRegistration registration = registry.addInterceptor(tokenInterceptor);
        //拦截路径
        registration.addPathPatterns("/**").excludePathPatterns("/auth/login", "/auth/register", "/auth/sendSms"
        ,"/auth/isExist","/auth/checkSms","/auth/forgetPassword");
    }

    /**
     * 跨域请求配置
     */
    //@Override
    //public void addCorsMappings(CorsRegistry registry) {
    //    //允许跨域的路径
    //    registry.addMapping("/**")
    //            .allowedOrigins("*")
    //            .allowCredentials(true)
    //            //允许跨域的方法
    //            .allowedMethods("GET","POST","PUT","DELETE")
    //            //允许跨域的请求头
    //            .allowedHeaders("*")
    //            .maxAge(3600);
    //}

}
