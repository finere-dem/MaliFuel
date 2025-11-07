package com.bamako.fuelqueue.service.impl;

import com.bamako.fuelqueue.domain.entity.Station;
import com.bamako.fuelqueue.domain.entity.Ticket;
import com.bamako.fuelqueue.domain.entity.User;
import com.bamako.fuelqueue.domain.entity.Vehicle;
import com.bamako.fuelqueue.domain.enumeration.FuelType;
import com.bamako.fuelqueue.domain.enumeration.TicketStatus;
import com.bamako.fuelqueue.domain.enumeration.UserRole;
import com.bamako.fuelqueue.domain.mapper.TicketMapper;
import com.bamako.fuelqueue.domain.repository.StationRepository;
import com.bamako.fuelqueue.domain.repository.TicketRepository;
import com.bamako.fuelqueue.domain.repository.VehicleRepository;
import com.bamako.fuelqueue.dto.request.TicketCreateRequest;
import com.bamako.fuelqueue.dto.response.TicketResponse;
import com.bamako.fuelqueue.exception.BadRequestException;
import com.bamako.fuelqueue.exception.ResourceNotFoundException;
import com.bamako.fuelqueue.exception.UnauthorizedException;
import com.bamako.fuelqueue.security.service.CurrentUserService;
import com.bamako.fuelqueue.service.QueueNotificationService;
import com.bamako.fuelqueue.service.QrCodeService;
import com.bamako.fuelqueue.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final StationRepository stationRepository;
    private final VehicleRepository vehicleRepository;
    private final TicketMapper ticketMapper;
    private final CurrentUserService currentUserService;
    private final QrCodeService qrCodeService;
    private final QueueNotificationService queueNotificationService;

    @Override
    public TicketResponse createTicket(TicketCreateRequest request) {
        User user = currentUserService.getCurrentUser();

        if (ticketRepository.existsByUserIdAndStatusIn(user.getId(),
            List.of(TicketStatus.WAITING))) {
            throw new BadRequestException("You already have an active ticket in queue.");
        }

        Station station = stationRepository.findById(request.getStationId())
            .orElseThrow(() -> new ResourceNotFoundException("Station not found."));

        if (!station.isRavitailed()) {
            throw new BadRequestException("Station is not currently ravitailed.");
        }

        Vehicle vehicle = resolveVehicle(user, request.getVehicleId());

        FuelType fuelType = request.getFuelType() != null ? request.getFuelType() : vehicle.getFuelType();

        if (fuelType != vehicle.getFuelType()) {
            throw new BadRequestException("Vehicle fuel type does not match requested fuel type.");
        }

        int queueSize = (int) ticketRepository.countByStationIdAndFuelTypeAndStatus(
            station.getId(), fuelType, TicketStatus.WAITING);

        Ticket ticket = Ticket.builder()
            .ticketCode(generateTicketCode(station.getId()))
            .user(user)
            .vehicle(vehicle)
            .station(station)
            .fuelType(fuelType)
            .requestedLiters(request.getRequestedLiters())
            .status(TicketStatus.WAITING)
            .position(queueSize + 1)
            .build();

        Ticket saved = ticketRepository.save(ticket);

        queueNotificationService.notifyQueueChanged(station.getId());

        TicketResponse response = ticketMapper.toDto(saved);
        response.setQrCodeBase64(qrCodeService.generateTicketQr(
            saved.getId().toString(), station.getId().toString()));
        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public TicketResponse getTicket(UUID ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
            .orElseThrow(() -> new ResourceNotFoundException("Ticket not found."));
        ensureTicketAccess(ticket);
        TicketResponse response = ticketMapper.toDto(ticket);
        response.setQrCodeBase64(qrCodeService.generateTicketQr(
            ticket.getId().toString(), ticket.getStation().getId().toString()));
        return response;
    }

    @Override
    public void cancelTicket(UUID ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
            .orElseThrow(() -> new ResourceNotFoundException("Ticket not found."));

        ensureTicketAccess(ticket);

        if (ticket.getStatus() != TicketStatus.WAITING) {
            throw new BadRequestException("Only waiting tickets can be cancelled.");
        }

        ticket.setStatus(TicketStatus.CANCELLED);
        ticket.setServedAt(OffsetDateTime.now());
        ticketRepository.save(ticket);

        recalculateQueue(ticket.getStation().getId(), ticket.getFuelType());
        queueNotificationService.notifyQueueChanged(ticket.getStation().getId());
    }

    private Vehicle resolveVehicle(User user, UUID vehicleId) {
        Vehicle vehicle;
        if (vehicleId != null) {
            vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found."));
            if (!vehicle.getUser().getId().equals(user.getId())) {
                throw new UnauthorizedException("Vehicle does not belong to current user.");
            }
        } else {
            vehicle = vehicleRepository.findByUserId(user.getId())
                .orElseThrow(() -> new BadRequestException("User has no registered vehicle."));
        }
        return vehicle;
    }

    private void ensureTicketAccess(Ticket ticket) {
        User user = currentUserService.getCurrentUser();
        if (!ticket.getUser().getId().equals(user.getId())
            && user.getRole() == UserRole.USAGER) {
            throw new UnauthorizedException("Not authorized to access this ticket.");
        }
    }

    private void recalculateQueue(UUID stationId, FuelType fuelType) {
        List<Ticket> waitingTickets = ticketRepository
            .findByStationIdAndFuelTypeAndStatusOrderByPositionAsc(stationId, fuelType, TicketStatus.WAITING);
        int index = 1;
        for (Ticket waitingTicket : waitingTickets) {
            waitingTicket.setPosition(index++);
        }
        ticketRepository.saveAll(waitingTickets);
    }

    private String generateTicketCode(UUID stationId) {
        return stationId.toString().substring(0, 8) + "-" + UUID.randomUUID().toString().substring(0, 8);
    }
}
