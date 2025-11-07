package com.bamako.fuelqueue.dto.request;

import com.bamako.fuelqueue.domain.enumeration.FuelType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.UUID;

@Getter
@Setter
public class StationCreateRequest {

    @NotBlank
    @Size(max = 150)
    private String name;

    @NotNull
    private UUID localityId;

    @NotNull
    private Double latitude;

    @NotNull
    private Double longitude;

    @NotNull
    private Boolean ravitailed;

    @NotNull
    private Map<FuelType, Integer> maxCapacityPerFuel;
}
