package com.hspedu.hspliving.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

/**
 * @author yangda
 * @create 2024-03-13-1:38
 * @description: 配置解决网关跨域问题，之前解决跨域问题的配置类需要实现
 * web-starter中的一个接口，但是网关gateway和web-starter同时引入会冲突
 * ，因此之前的解决跨域问题的配置类 `public class CorsConfig implements WebMvcConfigurer`  用不了
 *
 */
@Configuration
public class HsplivingGatewayCorsConfiguration {
    @Bean
    public CorsWebFilter corsWebFilter() {
        System.out.println("enter....");
        // 这里要选 org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource , 不要选错了
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        //1、配置跨域
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.addAllowedOrigin("*");
        corsConfiguration.setAllowCredentials(true);
        source.registerCorsConfiguration("/**", corsConfiguration);
        return new CorsWebFilter(source);
    }
}