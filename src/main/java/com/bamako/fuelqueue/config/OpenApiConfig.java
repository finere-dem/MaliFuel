package com.bamako.fuelqueue.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Fuel Queue Management API",
                version = "1.0",
                description = "API documentation for the Bamako fuel queue management platform",
                contact = @Contact(name = "Fuel Queue Team", email = "support@fuelqueue.bko")),
        servers = {
                @Server(url = "http://localhost:8080", description = "Local"),
                @Server(url = "https://fuelqueue.bko", description = "Production")
        }
)
public class OpenApiConfig {
}
