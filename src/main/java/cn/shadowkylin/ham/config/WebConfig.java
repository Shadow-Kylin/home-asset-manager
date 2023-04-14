package cn.shadowkylin.ham.config;

import cn.shadowkylin.ham.common.TokenInterceptor;
import com.google.gson.*;
import com.google.gson.internal.bind.util.ISO8601Utils;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;
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
        registration.addPathPatterns("/**").excludePathPatterns("/auth/login", "/auth/register", "/auth/sendSms");
    }

    /**
     * 解决gson与jackson冲突
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        //清除默认的转换器
        converters.clear();
        //添加gson转换器，不去除null值
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.serializeNulls();
        //使用统一日期格式
        gsonBuilder.setDateFormat("yyyy-MM-dd HH:mm:ss");
        Gson gson = gsonBuilder.create();
        GsonHttpMessageConverter gsonConverter = new GsonHttpMessageConverter(gson);
        converters.add(gsonConverter);
    }
}
