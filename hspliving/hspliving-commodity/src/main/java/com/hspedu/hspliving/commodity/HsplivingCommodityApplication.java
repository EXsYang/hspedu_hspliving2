package com.hspedu.hspliving.commodity;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author yangda
 * @create 2024-02-15-0:31
 * @description:
 */
// 如果Dao类上没有写@Mapper注解，则这里需要加上@MapperScan注解，指定要扫描的dao包
// ,如果dao包中的Mapper类上写了@Mapper注解，则这里可以不写
// @MapperScan("com.hspedu.hspliving.commodity.dao")
@EnableDiscoveryClient
@SpringBootApplication
public class HsplivingCommodityApplication {
    public static void main(String[] args) {
        //http://localhost:9090/commodity/category/info/1
        SpringApplication.run(HsplivingCommodityApplication.class,args);
    }
}
