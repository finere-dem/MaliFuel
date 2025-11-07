package com.bamako.fuelqueue.domain.dto.ticket;

import com.bamako.fuelqueue.domain.dto.common.LocalityDto;
import com.bamako.fuelqueue.domain.dto.common.VehicleDto;
import com.bamako.fuelqueue.domain.dto.station.StationResponse;
import com.bamako.fuelqueue.domain.enums.FuelType;
import com.bamako.fuelqueue.domain.enums.TicketStatus;
import java.time.Instant;
import java.util.UUID;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class TicketResponse {
    UUID id;
    String ticketCode;
    String qrCode;
    FuelType fuelType;
    int requestedLiters;
    TicketStatus status;
    int position;
    Instant createdAt;
    Instant updatedAt;
    Instant servedAt;
    VehicleDto vehicle;
    StationResponse station;
}
