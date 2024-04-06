package com.hspedu.hspliving.service.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

   // 下面这段代码，可以解决跨域问题
   @Override
   public void addCorsMappings(CorsRegistry registry) {
       // 为了解决返回多个返回头问题将其注销，跨域配置只需要配置一次即可
       // ，`跨域问题 已拦截跨源请求 不允许有多个 'Access-Control-Allow-Origin' CORS 头`

       // registry.addMapping("/**")
       //     .allowedOrigins("*")
       //     .allowCredentials(true)
       //     .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
       //     .maxAge(3600);
   }
}