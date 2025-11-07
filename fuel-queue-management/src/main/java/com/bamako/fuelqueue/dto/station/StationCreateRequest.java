package com.bamako.fuelqueue.dto.station;

import com.bamako.fuelqueue.enums.FuelType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.EnumMap;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
public class StationCreateRequest {

    @NotBlank
    private String name;

    @NotNull
    private UUID localityId;

    @NotNull
    private Double latitude;

    @NotNull
    private Double longitude;

    private boolean ravitailed;

    @NotNull
    @Size(min = 1, message = "At least one fuel capacity must be provided")
    private Map<FuelType, Integer> maxCapacityPerFuel = new EnumMap<>(FuelType.class);
}
