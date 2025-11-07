package com.bamako.fuelqueue.dto.ticket;

import com.bamako.fuelqueue.dto.station.StationSummary;
import com.bamako.fuelqueue.dto.user.VehicleResponse;
import com.bamako.fuelqueue.enums.FuelType;
import com.bamako.fuelqueue.enums.TicketStatus;
import lombok.Builder;
import lombok.Value;

import java.time.Instant;
import java.util.UUID;

@Value
@Builder
public class TicketResponse {
    UUID id;
    String ticketCode;
    UUID userId;
    VehicleResponse vehicle;
    StationSummary station;
    FuelType fuelType;
    Integer requestedLiters;
    TicketStatus status;
    Integer position;
    Instant createdAt;
    Instant updatedAt;
    Instant servedAt;
    String qrCode;
}
