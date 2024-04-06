package com.hspedu.hspliving.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author yangda
 * @create 2024-03-08-21:40
 * @description:
 */
@EnableDiscoveryClient
@SpringBootApplication
public class HsplivingServiceApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext =
                SpringApplication.run(HsplivingServiceApplication.class, args);

        System.out.println("HsplivingServiceApplication starting...");

    }
}
