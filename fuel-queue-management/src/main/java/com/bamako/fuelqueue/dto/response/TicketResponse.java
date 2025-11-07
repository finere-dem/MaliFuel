package com.bamako.fuelqueue.dto.response;

import com.bamako.fuelqueue.domain.enumeration.FuelType;
import com.bamako.fuelqueue.domain.enumeration.TicketStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
public class TicketResponse {
    private UUID id;
    private String ticketCode;
    private FuelType fuelType;
    private int requestedLiters;
    private TicketStatus status;
    private int position;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private OffsetDateTime servedAt;
    private StationResponse station;
    private VehicleResponse vehicle;
    private String qrCodeBase64;
}
