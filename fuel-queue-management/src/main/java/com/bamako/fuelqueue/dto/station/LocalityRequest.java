package com.bamako.fuelqueue.dto.station;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LocalityRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String region;
}
