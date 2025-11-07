package com.bamako.fuelqueue.dto.station;

import com.bamako.fuelqueue.enums.FuelType;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.EnumMap;
import java.util.Map;

@Getter
@Setter
public class StationUpdateRequest {

    private String name;
    private Double latitude;
    private Double longitude;
    private Boolean ravitailed;

    @Size(min = 1, message = "At least one fuel capacity must be provided")
    private Map<FuelType, Integer> maxCapacityPerFuel = new EnumMap<>(FuelType.class);
}
