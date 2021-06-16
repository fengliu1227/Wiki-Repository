//package com.andrew.wiki.config;
//
//import com.andrew.wiki.interceptor.LoginInterceptor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//
//@Configuration
//public class SpringMvcConfig implements WebMvcConfigurer {
//
//    @Autowired
//    LoginInterceptor loginInterceptor;
//
//
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(loginInterceptor)
//                .addPathPatterns("/**")
//                .excludePathPatterns("/user/login",
//                        "/category/list",
//                        "/ebook/list",
//                        "/doc/all/**",
//                        "/doc/list",
//                        "/content/**",
//                        "/category",
//                        "/redis/**",
//                        "/content/**"
//                );
//    }
//}
