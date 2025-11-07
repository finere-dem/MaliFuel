package com.bamako.fuelqueue.dto.ticket;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TicketValidationRequest {

    @Min(value = 1, message = "Liters supplied must be greater than zero")
    private int litersSupplied;
}
