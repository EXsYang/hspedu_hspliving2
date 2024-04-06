package com.hspedu.hspliving.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author yangda
 * @create 2024-03-11-23:48
 * @description:
 */
@EnableDiscoveryClient
@SpringBootApplication
public class HsplivingGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(HsplivingGatewayApplication.class,args);
        System.out.println("HsplivingGatewayApplication starting...");
    }
}
