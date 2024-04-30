package com.ttodampartners.ttodamttodam.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

    @Configuration
    public class WebConfig implements WebMvcConfigurer {

        @Override
        public void addCorsMappings(CorsRegistry registry) {
            registry.addMapping("/**")
                    .allowedOrigins("http://localhost:5174")// 허용할 출처
                    .allowedMethods("GET", "POST", "PUT", "DELETE")// 허용할 HTTP 메소드
                    .allowedHeaders("*");// 허용할 요청 헤더
        }
    }
