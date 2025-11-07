package com.bamako.fuelqueue.security.jwt;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "security.jwt")
public class JwtProperties {

    @NotBlank
    private String secret;

    @NotNull
    private Long expiration;

    @NotBlank
    private String issuer;
}
