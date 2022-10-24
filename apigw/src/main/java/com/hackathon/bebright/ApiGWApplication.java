package com.hackathon.bebright;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(
        basePackages = "com.hackathon.bebright.clients.users"
)
public class ApiGWApplication {
    public static void main(String[] args) {
        SpringApplication.run(ApiGWApplication.class, args);
    }


}
