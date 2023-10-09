//package com.jiawa.wiki.config;
//
//import com.jiawa.wiki.interceptor.LogInterceptor;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//import javax.annotation.Resource;
//
//@Configuration
//public class SpringMvcConfig implements WebMvcConfigurer {
//
//    @Resource
//    LogInterceptor logInterceptor;
//
//    public void addInterceptors(InterceptorRegistry registry){
//        registry.addInterceptor(logInterceptor)
//                .addPathPatterns("/**").excludePathPatterns("/login"); //针对所有请求都拦截，除了登录接口
//    }
//}
