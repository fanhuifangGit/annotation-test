package com.fanhf.annotationtest.interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author fanhf
 * @Description 添加校验的拦截器config
 * @date 2020-11-16 12:35
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    CheckParamsInterceptor checkParamsInterceptor = new CheckParamsInterceptor();

    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(checkParamsInterceptor).addPathPatterns("/**");
    }
}   
