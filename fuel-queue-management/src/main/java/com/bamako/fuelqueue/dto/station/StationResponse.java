package com.bamako.fuelqueue.dto.station;

import com.bamako.fuelqueue.enums.FuelType;
import lombok.Builder;
import lombok.Value;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Value
@Builder
public class StationResponse {
    UUID id;
    String name;
    UUID localityId;
    String localityName;
    String region;
    Double latitude;
    Double longitude;
    boolean ravitailed;
    Map<FuelType, Integer> maxCapacityPerFuel;
    Instant createdAt;
    Instant updatedAt;
}
