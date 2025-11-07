package com.bamako.fuelqueue.dto.request;

import com.bamako.fuelqueue.domain.enumeration.FuelType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VehicleRegistrationRequest {

    @NotBlank
    @Size(max = 20)
    private String plateNumber;

    @NotBlank
    @Size(max = 100)
    private String make;

    @NotBlank
    @Size(max = 100)
    private String model;

    @NotNull
    private FuelType fuelType;
}
