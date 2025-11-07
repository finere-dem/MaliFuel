package com.bamako.fuelqueue.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
public class ConsumptionRecordResponse {
    private UUID id;
    private TicketResponse ticket;
    private StationResponse station;
    private int litersSupplied;
    private UserResponse validatedBy;
    private OffsetDateTime confirmationTime;
}
