package com.bamako.fuelqueue.service;

import com.bamako.fuelqueue.dto.ticket.TicketResponse;
import com.bamako.fuelqueue.dto.ticket.TicketValidationRequest;
import com.bamako.fuelqueue.entity.ConsumptionRecord;
import com.bamako.fuelqueue.entity.Ticket;
import com.bamako.fuelqueue.entity.User;
import com.bamako.fuelqueue.enums.Role;
import com.bamako.fuelqueue.enums.TicketStatus;
import com.bamako.fuelqueue.exception.BadRequestException;
import com.bamako.fuelqueue.exception.ResourceNotFoundException;
import com.bamako.fuelqueue.mapper.TicketMapper;
import com.bamako.fuelqueue.repository.ConsumptionRecordRepository;
import com.bamako.fuelqueue.repository.UserRepository;
import com.bamako.fuelqueue.security.SecurityUtils;
import com.bamako.fuelqueue.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GerantService {

    private final TicketService ticketService;
    private final TicketMapper ticketMapper;
    private final ConsumptionRecordRepository consumptionRecordRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<TicketResponse> getWaitingTickets(UUID stationId) {
        return ticketService.getWaitingTickets(stationId).stream()
                .map(ticketMapper::toResponse)
                .toList();
    }

    @Transactional
    public TicketResponse validateTicket(UUID stationId, UUID ticketId, TicketValidationRequest request) {
        UserPrincipal principal = SecurityUtils.getCurrentUserPrincipal();
        if (principal == null || principal.getRole() == Role.USAGER) {
            throw new BadRequestException("Only station managers can validate tickets");
        }

        Ticket ticket = ticketService.getTicketEntity(ticketId);

        if (!ticket.getStation().getId().equals(stationId)) {
            throw new BadRequestException("Ticket does not belong to the provided station");
        }

        if (ticket.getStatus() != TicketStatus.WAITING) {
            throw new BadRequestException("Only waiting tickets can be validated");
        }

        User gerant = userRepository.findById(principal.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Authenticated manager not found"));

        ticketService.markTicketAsServed(ticket);

        ConsumptionRecord consumptionRecord = ConsumptionRecord.builder()
                .ticket(ticket)
                .station(ticket.getStation())
                .litersSupplied(request.getLitersSupplied())
                .validatedBy(gerant)
                .confirmationTime(Instant.now())
                .build();
        consumptionRecordRepository.save(consumptionRecord);

        return ticketMapper.toResponse(ticket);
    }
}
