package com.bamako.fuelqueue;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@ConfigurationPropertiesScan
public class FuelQueueManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(FuelQueueManagementApplication.class, args);
    }
}
