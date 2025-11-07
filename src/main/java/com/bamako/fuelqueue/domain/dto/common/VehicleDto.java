package com.bamako.fuelqueue.domain.dto.common;

import com.bamako.fuelqueue.domain.enums.FuelType;
import java.util.UUID;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class VehicleDto {
    UUID id;
    String plateNumber;
    String make;
    String model;
    FuelType fuelType;
}
