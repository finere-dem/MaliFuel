package com.bamako.fuelqueue;

import com.bamako.fuelqueue.security.JwtProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(JwtProperties.class)
public class FuelQueueManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(FuelQueueManagementApplication.class, args);
    }
}
