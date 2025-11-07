package com.bamako.fuelqueue.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AuthRegisterRequest {

    @NotBlank
    @Pattern(regexp = "^[0-9+]{8,15}$", message = "Phone number must be valid.")
    private String phone;

    @NotBlank
    @Size(min = 6, max = 100)
    private String password;

    @NotBlank
    @Size(max = 100)
    private String firstName;

    @NotBlank
    @Size(max = 100)
    private String lastName;

    @Valid
    @NotNull
    private VehicleRegistrationRequest vehicle;

    @Valid
    private List<UserLocalityRegistrationRequest> localities;
}
