package com.bamako.fuelqueue.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthLoginRequest {

    @NotBlank
    @Pattern(regexp = "^[0-9+]{8,15}$", message = "Phone number must be valid.")
    private String phone;

    @NotBlank
    private String password;
}
