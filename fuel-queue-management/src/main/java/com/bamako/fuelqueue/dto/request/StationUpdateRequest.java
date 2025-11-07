package com.bamako.fuelqueue.dto.request;

import com.bamako.fuelqueue.domain.enumeration.FuelType;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.UUID;

@Getter
@Setter
public class StationUpdateRequest {

    @Size(min = 1, max = 150)
    private String name;

    private UUID localityId;

    private Double latitude;

    private Double longitude;

    private Boolean ravitailed;

    private Map<FuelType, Integer> maxCapacityPerFuel;
}
