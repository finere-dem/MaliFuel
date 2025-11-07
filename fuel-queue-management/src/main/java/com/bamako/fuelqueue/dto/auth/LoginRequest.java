package com.bamako.fuelqueue.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {

    @NotBlank
    private String phone;

    @NotBlank
    private String password;
}
