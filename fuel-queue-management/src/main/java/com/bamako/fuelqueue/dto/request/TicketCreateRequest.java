package com.bamako.fuelqueue.dto.request;

import com.bamako.fuelqueue.domain.enumeration.FuelType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class TicketCreateRequest {

    @NotNull
    private UUID stationId;

    private UUID vehicleId;

    @NotNull
    private FuelType fuelType;

    @Min(1)
    private int requestedLiters;
}
