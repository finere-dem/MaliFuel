package com.bamako.fuelqueue.dto.response;

import com.bamako.fuelqueue.domain.enumeration.FuelType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@Builder
public class StationResponse {
    private UUID id;
    private String name;
    private LocalityResponse locality;
    private Double latitude;
    private Double longitude;
    private boolean ravitailed;
    private Map<FuelType, Integer> maxCapacityPerFuel;
    private OffsetDateTime createdAt;
}
