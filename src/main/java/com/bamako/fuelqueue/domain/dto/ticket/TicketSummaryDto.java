package com.bamako.fuelqueue.domain.dto.ticket;

import com.bamako.fuelqueue.domain.enums.FuelType;
import com.bamako.fuelqueue.domain.enums.TicketStatus;
import java.time.Instant;
import java.util.UUID;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class TicketSummaryDto {
    UUID id;
    String ticketCode;
    FuelType fuelType;
    int requestedLiters;
    TicketStatus status;
    int position;
    Instant createdAt;
    Instant servedAt;
    String userFirstName;
    String userLastName;
    String userPhone;
    String vehiclePlate;
}
