package com.bamako.fuelqueue.dto.auth;

import com.bamako.fuelqueue.enums.FuelType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class RegisterRequest {

    @NotBlank
    private String phone;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    private String password;

    @Valid
    @NotNull
    private VehicleRequest vehicle;

    @Valid
    @Size(min = 1, max = 2, message = "You can register up to 2 localities")
    private List<UserLocalityRequest> localities = new ArrayList<>();

    @Getter
    @Setter
    public static class VehicleRequest {
        @NotBlank
        private String plateNumber;

        @NotBlank
        private String make;

        @NotBlank
        private String model;

        @NotNull
        private FuelType fuelType;
    }

    @Getter
    @Setter
    public static class UserLocalityRequest {
        @NotNull
        private UUID localityId;

        private boolean primary;
    }
}
