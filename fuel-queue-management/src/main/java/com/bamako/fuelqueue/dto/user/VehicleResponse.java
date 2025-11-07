package com.bamako.fuelqueue.dto.user;

import com.bamako.fuelqueue.enums.FuelType;
import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class VehicleResponse {
    UUID id;
    String plateNumber;
    String make;
    String model;
    FuelType fuelType;
}
