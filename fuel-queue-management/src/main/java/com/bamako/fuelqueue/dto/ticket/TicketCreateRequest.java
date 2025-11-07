package com.bamako.fuelqueue.dto.ticket;

import com.bamako.fuelqueue.enums.FuelType;
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

    @NotNull
    private FuelType fuelType;

    @Min(value = 1, message = "Requested liters must be greater than zero")
    private int requestedLiters;
}
