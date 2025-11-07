package com.bamako.fuelqueue.dto.response;

import com.bamako.fuelqueue.domain.enumeration.FuelType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
public class VehicleResponse {
    private UUID id;
    private String plateNumber;
    private String make;
    private String model;
    private FuelType fuelType;
    private OffsetDateTime createdAt;
}
