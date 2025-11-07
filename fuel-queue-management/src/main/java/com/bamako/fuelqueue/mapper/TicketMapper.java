package com.bamako.fuelqueue.mapper;

import com.bamako.fuelqueue.dto.ticket.TicketResponse;
import com.bamako.fuelqueue.entity.Ticket;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TicketMapper {

    private final UserMapper userMapper;
    private final StationMapper stationMapper;

    public TicketResponse toResponse(Ticket ticket) {
        return toResponse(ticket, null);
    }

    public TicketResponse toResponse(Ticket ticket, String qrCode) {
        if (ticket == null) {
            return null;
        }
        return TicketResponse.builder()
                .id(ticket.getId())
                .ticketCode(ticket.getTicketCode())
                .userId(ticket.getUser().getId())
                .vehicle(userMapper.toVehicleResponse(ticket.getVehicle()))
                .station(stationMapper.toSummary(ticket.getStation()))
                .fuelType(ticket.getFuelType())
                .requestedLiters(ticket.getRequestedLiters())
                .status(ticket.getStatus())
                .position(ticket.getPosition())
                .createdAt(ticket.getCreatedAt())
                .updatedAt(ticket.getUpdatedAt())
                .servedAt(ticket.getServedAt())
                .qrCode(qrCode)
                .build();
    }
}
