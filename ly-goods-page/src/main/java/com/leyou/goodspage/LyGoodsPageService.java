package com.leyou.goodspage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient //注册中心客户端
@EnableFeignClients //调用其他服务模块API
public class LyGoodsPageService {
    public static void main(String[] args) {
        SpringApplication.run(LyGoodsPageService.class,args);
    }
}
