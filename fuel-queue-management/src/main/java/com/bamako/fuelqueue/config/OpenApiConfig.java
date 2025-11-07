package com.bamako.fuelqueue.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI fuelQueueOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Fuel Queue Management API")
                .description("API documentation for the Bamako Fuel Queue Management System")
                .version("1.0.0")
                .contact(new Contact().name("Fuel Queue Team").email("support@bamakofuel.com")));
    }
}
