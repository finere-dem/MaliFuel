package com.bamako.fuelqueue.dto.request;

import com.bamako.fuelqueue.domain.enumeration.FuelType;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VehicleUpdateRequest {

    @Size(min = 1, max = 20)
    private String plateNumber;

    @Size(min = 1, max = 100)
    private String make;

    @Size(min = 1, max = 100)
    private String model;

    private FuelType fuelType;
}
