package com.bamako.fuelqueue.service.impl;

import com.bamako.fuelqueue.domain.entity.ConsumptionRecord;
import com.bamako.fuelqueue.domain.entity.Station;
import com.bamako.fuelqueue.domain.entity.Ticket;
import com.bamako.fuelqueue.domain.entity.User;
import com.bamako.fuelqueue.domain.enumeration.TicketStatus;
import com.bamako.fuelqueue.domain.enumeration.UserRole;
import com.bamako.fuelqueue.domain.mapper.TicketMapper;
import com.bamako.fuelqueue.domain.repository.ConsumptionRecordRepository;
import com.bamako.fuelqueue.domain.repository.StationRepository;
import com.bamako.fuelqueue.domain.repository.TicketRepository;
import com.bamako.fuelqueue.dto.request.TicketValidationRequest;
import com.bamako.fuelqueue.dto.response.QueueTicketResponse;
import com.bamako.fuelqueue.exception.BadRequestException;
import com.bamako.fuelqueue.exception.ResourceNotFoundException;
import com.bamako.fuelqueue.exception.UnauthorizedException;
import com.bamako.fuelqueue.security.service.CurrentUserService;
import com.bamako.fuelqueue.service.GerantService;
import com.bamako.fuelqueue.service.QueueNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class GerantServiceImpl implements GerantService {

    private final TicketRepository ticketRepository;
    private final StationRepository stationRepository;
    private final ConsumptionRecordRepository consumptionRecordRepository;
    private final TicketMapper ticketMapper;
    private final CurrentUserService currentUserService;
    private final QueueNotificationService queueNotificationService;

    @Override
    @Transactional(readOnly = true)
    public List<QueueTicketResponse> getWaitingTickets(UUID stationId) {
        ensureGerantAccess();
        Station station = stationRepository.findById(stationId)
            .orElseThrow(() -> new ResourceNotFoundException("Station not found."));

        return ticketRepository.findByStationIdAndStatusOrderByPositionAsc(station.getId(), TicketStatus.WAITING)
            .stream()
            .map(ticketMapper::toQueueDto)
            .collect(Collectors.toList());
    }

    @Override
    public QueueTicketResponse validateTicket(UUID stationId, UUID ticketId, TicketValidationRequest request) {
        User current = ensureGerantAccess();
        Station station = stationRepository.findById(stationId)
            .orElseThrow(() -> new ResourceNotFoundException("Station not found."));
        Ticket ticket = ticketRepository.findById(ticketId)
            .orElseThrow(() -> new ResourceNotFoundException("Ticket not found."));

        if (!ticket.getStation().getId().equals(station.getId())) {
            throw new BadRequestException("Ticket does not belong to this station.");
        }

        if (ticket.getStatus() != TicketStatus.WAITING) {
            throw new BadRequestException("Only waiting tickets can be validated.");
        }

        ticket.setStatus(TicketStatus.SERVED);
        ticket.setServedAt(OffsetDateTime.now());
        ticketRepository.save(ticket);

        ConsumptionRecord record = ConsumptionRecord.builder()
            .ticket(ticket)
            .station(station)
            .litersSupplied(request.getLitersSupplied())
            .validatedBy(current)
            .confirmationTime(OffsetDateTime.now())
            .build();
        consumptionRecordRepository.save(record);

        recalculateQueue(station.getId(), ticket.getFuelType());
        queueNotificationService.notifyQueueChanged(station.getId());

        return ticketMapper.toQueueDto(ticket);
    }

    private User ensureGerantAccess() {
        User user = currentUserService.getCurrentUser();
        if (user.getRole() != UserRole.GERANT && user.getRole() != UserRole.ADMIN) {
            throw new UnauthorizedException("Gérant privileges required.");
        }
        return user;
    }

    private void recalculateQueue(UUID stationId, com.bamako.fuelqueue.domain.enumeration.FuelType fuelType) {
        List<Ticket> waitingTickets = ticketRepository
            .findByStationIdAndFuelTypeAndStatusOrderByPositionAsc(stationId, fuelType, TicketStatus.WAITING);
        int position = 1;
        for (Ticket waitingTicket : waitingTickets) {
            waitingTicket.setPosition(position++);
        }
        ticketRepository.saveAll(waitingTickets);
    }
}
