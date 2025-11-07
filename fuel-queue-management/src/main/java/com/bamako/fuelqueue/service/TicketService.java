package com.bamako.fuelqueue.service;

import com.bamako.fuelqueue.dto.ticket.TicketCreateRequest;
import com.bamako.fuelqueue.dto.ticket.TicketResponse;
import com.bamako.fuelqueue.entity.Station;
import com.bamako.fuelqueue.entity.Ticket;
import com.bamako.fuelqueue.entity.User;
import com.bamako.fuelqueue.entity.Vehicle;
import com.bamako.fuelqueue.enums.FuelType;
import com.bamako.fuelqueue.enums.Role;
import com.bamako.fuelqueue.enums.TicketStatus;
import com.bamako.fuelqueue.exception.BadRequestException;
import com.bamako.fuelqueue.exception.ResourceNotFoundException;
import com.bamako.fuelqueue.mapper.TicketMapper;
import com.bamako.fuelqueue.repository.StationRepository;
import com.bamako.fuelqueue.repository.TicketRepository;
import com.bamako.fuelqueue.repository.UserRepository;
import com.bamako.fuelqueue.repository.VehicleRepository;
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
public class TicketService {

    private final TicketRepository ticketRepository;
    private final StationRepository stationRepository;
    private final UserRepository userRepository;
    private final VehicleRepository vehicleRepository;
    private final TicketMapper ticketMapper;
    private final QrCodeService qrCodeService;
    private final QueueNotificationService queueNotificationService;

    @Transactional
    public TicketResponse createTicket(TicketCreateRequest request) {
        User user = getAuthenticatedUser();

        if (ticketRepository.existsByUserIdAndStatus(user.getId(), TicketStatus.WAITING)) {
            throw new BadRequestException("User already has a waiting ticket");
        }

        Vehicle vehicle = vehicleRepository.findByUserId(user.getId())
                .orElseThrow(() -> new BadRequestException("User has no registered vehicle"));

        Station station = stationRepository.findById(request.getStationId())
                .orElseThrow(() -> new ResourceNotFoundException("Station not found"));

        if (!supportsFuelType(station, request.getFuelType())) {
            throw new BadRequestException("Station does not support the requested fuel type");
        }

        int nextPosition = ticketRepository.countByStationIdAndFuelTypeAndStatus(
                station.getId(), request.getFuelType(), TicketStatus.WAITING) + 1;

        Ticket ticket = Ticket.builder()
                .ticketCode(generateTicketCode())
                .user(user)
                .vehicle(vehicle)
                .station(station)
                .fuelType(request.getFuelType())
                .requestedLiters(request.getRequestedLiters())
                .status(TicketStatus.WAITING)
                .position(nextPosition)
                .build();
        ticketRepository.save(ticket);

        String qrContent = ticket.getId() + ":" + station.getId();
        String qrCode = qrCodeService.generateQrCodeBase64(qrContent);

        queueNotificationService.broadcastQueueUpdate(station.getId());

        return ticketMapper.toResponse(ticket, qrCode);
    }

    @Transactional(readOnly = true)
    public TicketResponse getTicket(UUID ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found"));
        enforceTicketAccess(ticket);
        String qrCode = ticket.getStatus() == TicketStatus.WAITING
                ? qrCodeService.generateQrCodeBase64(ticket.getId() + ":" + ticket.getStation().getId())
                : null;
        return ticketMapper.toResponse(ticket, qrCode);
    }

    @Transactional
    public void cancelTicket(UUID ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found"));
        enforceTicketAccess(ticket);

        if (ticket.getStatus() != TicketStatus.WAITING) {
            throw new BadRequestException("Only waiting tickets can be cancelled");
        }

        ticket.setStatus(TicketStatus.CANCELLED);
        ticket.setServedAt(null);
        ticketRepository.save(ticket);
        reorderQueue(ticket.getStation().getId(), ticket.getFuelType());
        queueNotificationService.broadcastQueueUpdate(ticket.getStation().getId());
    }

    @Transactional(readOnly = true)
    public List<Ticket> getWaitingTickets(UUID stationId) {
        return ticketRepository.findByStationIdAndStatusInOrderByPositionAsc(stationId, List.of(TicketStatus.WAITING));
    }

    @Transactional
    public void markTicketAsServed(Ticket ticket) {
        ticket.setStatus(TicketStatus.SERVED);
        ticket.setServedAt(Instant.now());
        ticketRepository.save(ticket);
        reorderQueue(ticket.getStation().getId(), ticket.getFuelType());
        queueNotificationService.broadcastQueueUpdate(ticket.getStation().getId());
    }

    @Transactional(readOnly = true)
    public Ticket getTicketEntity(UUID ticketId) {
        return ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found"));
    }

    private void reorderQueue(UUID stationId, FuelType fuelType) {
        List<Ticket> waitingTickets = ticketRepository
                .findByStationIdAndFuelTypeAndStatusOrderByPositionAsc(stationId, fuelType, TicketStatus.WAITING);
        int position = 1;
        for (Ticket waiting : waitingTickets) {
            waiting.setPosition(position++);
        }
        ticketRepository.saveAll(waitingTickets);
    }

    private User getAuthenticatedUser() {
        UUID userId = SecurityUtils.getCurrentUserId();
        if (userId == null) {
            throw new BadRequestException("No authenticated user available");
        }
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Authenticated user not found"));
    }

    private void enforceTicketAccess(Ticket ticket) {
        UserPrincipal principal = SecurityUtils.getCurrentUserPrincipal();
        if (principal == null) {
            throw new BadRequestException("No authenticated user available");
        }
        if (principal.getRole() == Role.ADMIN || principal.getRole() == Role.GERANT) {
            return;
        }
        if (!ticket.getUser().getId().equals(principal.getUserId())) {
            throw new BadRequestException("You are not allowed to access this ticket");
        }
    }

    private boolean supportsFuelType(Station station, FuelType fuelType) {
        return station.getMaxCapacityPerFuel() != null && station.getMaxCapacityPerFuel().containsKey(fuelType);
    }

    private String generateTicketCode() {
        return "TKT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
