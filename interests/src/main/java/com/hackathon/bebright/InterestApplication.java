package com.hackathon.bebright;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@EnableCaching
public class InterestApplication {
    public static void main(String[] args) {
        SpringApplication.run(InterestApplication.class, args);
    }

}
