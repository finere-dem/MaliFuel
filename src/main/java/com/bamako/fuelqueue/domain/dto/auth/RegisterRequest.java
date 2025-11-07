package com.bamako.fuelqueue.domain.dto.auth;

import com.bamako.fuelqueue.domain.enums.FuelType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.List;
import java.util.UUID;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank
    @Pattern(regexp = "^\\+?[0-9]{6,15}$", message = "Invalid phone number")
    private String phone;

    @NotBlank
    @Size(min = 2, max = 100)
    private String firstName;

    @NotBlank
    @Size(min = 2, max = 100)
    private String lastName;

    @NotBlank
    @Size(min = 6, max = 100)
    private String password;

    @Valid
    @NotNull
    private VehiclePayload vehicle;

    @Valid
    @NotEmpty
    private List<UserLocalityPayload> localities;

    @Data
    public static class VehiclePayload {
        @NotBlank
        private String plateNumber;
        @NotBlank
        private String make;
        @NotBlank
        private String model;
        @NotNull
        private FuelType fuelType;
    }

    @Data
    public static class UserLocalityPayload {
        @NotNull
        private UUID localityId;
        private boolean primary;
    }
}
