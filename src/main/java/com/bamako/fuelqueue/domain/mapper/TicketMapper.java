package com.bamako.fuelqueue.domain.mapper;

import com.bamako.fuelqueue.domain.dto.ticket.TicketResponse;
import com.bamako.fuelqueue.domain.dto.ticket.TicketSummaryDto;
import com.bamako.fuelqueue.domain.entity.Ticket;

public final class TicketMapper {

    private TicketMapper() {
    }

    public static TicketResponse toDto(Ticket ticket) {
        return toDto(ticket, null);
    }

    public static TicketResponse toDto(Ticket ticket, String qrCode) {
        if (ticket == null) {
            return null;
        }
        return TicketResponse.builder()
                .id(ticket.getId())
                .ticketCode(ticket.getTicketCode())
                .qrCode(qrCode)
                .fuelType(ticket.getFuelType())
                .requestedLiters(ticket.getRequestedLiters())
                .status(ticket.getStatus())
                .position(ticket.getPosition())
                .createdAt(ticket.getCreatedAt())
                .updatedAt(ticket.getUpdatedAt())
                .servedAt(ticket.getServedAt())
                .vehicle(VehicleMapper.toDto(ticket.getVehicle()))
                .station(StationMapper.toDto(ticket.getStation()))
                .build();
    }

    public static TicketSummaryDto toSummary(Ticket ticket) {
        if (ticket == null) {
            return null;
        }
        return TicketSummaryDto.builder()
                .id(ticket.getId())
                .ticketCode(ticket.getTicketCode())
                .fuelType(ticket.getFuelType())
                .requestedLiters(ticket.getRequestedLiters())
                .status(ticket.getStatus())
                .position(ticket.getPosition())
                .createdAt(ticket.getCreatedAt())
                .servedAt(ticket.getServedAt())
                .userFirstName(ticket.getUser().getFirstName())
                .userLastName(ticket.getUser().getLastName())
                .userPhone(ticket.getUser().getPhone())
                .vehiclePlate(ticket.getVehicle().getPlateNumber())
                .build();
    }
}
