package cn.shadowkylin.ham.config;

import cn.shadowkylin.ham.common.TokenInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.resource.PathResourceResolver;

import javax.annotation.Resource;
import java.io.IOException;

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
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        //允许跨域的路径
        registry.addMapping("/**")
                .allowedOrigins("*")
                //允许跨域的方法
                .allowedMethods("GET","POST","PUT","DELETE","OPTIONS","HEAD","PATCH")
                //允许跨域的请求头
                .allowedHeaders("*");
    }

    /**
     * 解决gson与jackson冲突
     */
    //@Override
    //public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
    //    //清除默认的转换器
    //    converters.clear();
    //    //添加gson转换器，不去除null值
    //    GsonBuilder gsonBuilder = new GsonBuilder();
    //    gsonBuilder.serializeNulls();
    //    //使用统一日期格式yyyy-MM-dd HH:mm:ss
    //    gsonBuilder.setDateFormat("yyyy-MM-dd HH:mm:ss");
    //    Gson gson = gsonBuilder.create();
    //    GsonHttpMessageConverter gsonConverter = new GsonHttpMessageConverter(gson);
    //    converters.add(gsonConverter);
    //}
}
