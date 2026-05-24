package com.restaurant.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")           // 所有接口
                .allowedOrigins("*")         // 允许所有来源
                .allowedMethods("*")         // 允许所有请求方法 (GET, POST, PUT, DELETE等)
                .allowedHeaders("*")         // 允许所有请求头
                .allowCredentials(false);    // 不允许携带凭证（因为 allowedOrigins 为 * 时需设为 false）
    }
}