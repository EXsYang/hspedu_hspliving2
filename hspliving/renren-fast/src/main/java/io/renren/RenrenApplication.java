
package io.renren;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


@EnableDiscoveryClient
@SpringBootApplication
public class RenrenApplication {


	public static void main(String[] args) {

		//new 后端项目
		// 访问 http://localhost:8080/renren-fast/ 进行测试
		// 如果返回 {"msg":"invalid token","code":401} ,则后端脚手架搭建成功！

		SpringApplication.run(RenrenApplication.class, args);
	}

}