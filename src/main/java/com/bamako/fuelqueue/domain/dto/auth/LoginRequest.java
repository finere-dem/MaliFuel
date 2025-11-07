package com.bamako.fuelqueue.domain.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginRequest {

    @NotBlank
    @Pattern(regexp = "^\\+?[0-9]{6,15}$")
    private String phone;

    @NotBlank
    @Size(min = 6, max = 100)
    private String password;
}
