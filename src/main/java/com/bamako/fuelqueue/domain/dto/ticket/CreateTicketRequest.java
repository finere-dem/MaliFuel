package com.bamako.fuelqueue.domain.dto.ticket;

import com.bamako.fuelqueue.domain.enums.FuelType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Data;

@Data
public class CreateTicketRequest {

    @NotNull
    private UUID stationId;

    @NotNull
    private UUID vehicleId;

    @NotNull
    private FuelType fuelType;

    @Min(1)
    private int requestedLiters;
}
