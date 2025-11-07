package com.bamako.fuelqueue.domain.dto.station;

import com.bamako.fuelqueue.domain.enums.FuelType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.EnumMap;
import java.util.Map;
import java.util.UUID;
import lombok.Data;

@Data
public class UpdateStationRequest {

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
    private Map<FuelType, @Positive Integer> maxCapacityPerFuel = new EnumMap<>(FuelType.class);
}
