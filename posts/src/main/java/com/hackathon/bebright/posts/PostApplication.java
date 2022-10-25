package com.hackathon.bebright.posts;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(
        basePackages = "com.hackathon.bebright.clients"
)
public class PostApplication {
    public static void main(String[] args) {
        SpringApplication.run(PostApplication.class, args);
    }
}
