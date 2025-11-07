package com.bamako.fuelqueue.domain.dto.station;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpsertLocalityRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String region;
}
