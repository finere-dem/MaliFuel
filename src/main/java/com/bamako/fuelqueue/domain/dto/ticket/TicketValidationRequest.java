package com.bamako.fuelqueue.domain.dto.ticket;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TicketValidationRequest {

    @NotBlank
    private String qrContent;
}
