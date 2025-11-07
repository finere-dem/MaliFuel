package com.bamako.fuelqueue.service.impl;

import com.bamako.fuelqueue.domain.dto.ticket.CreateTicketRequest;
import com.bamako.fuelqueue.domain.dto.ticket.QueueUpdateMessage;
import com.bamako.fuelqueue.domain.dto.ticket.TicketResponse;
import com.bamako.fuelqueue.domain.dto.ticket.TicketSummaryDto;
import com.bamako.fuelqueue.domain.entity.ConsumptionRecord;
import com.bamako.fuelqueue.domain.entity.Station;
import com.bamako.fuelqueue.domain.entity.Ticket;
import com.bamako.fuelqueue.domain.entity.User;
import com.bamako.fuelqueue.domain.entity.Vehicle;
import com.bamako.fuelqueue.domain.enums.FuelType;
import com.bamako.fuelqueue.domain.enums.TicketStatus;
import com.bamako.fuelqueue.domain.enums.UserRole;
import com.bamako.fuelqueue.domain.mapper.TicketMapper;
import com.bamako.fuelqueue.exception.BadRequestException;
import com.bamako.fuelqueue.exception.ForbiddenException;
import com.bamako.fuelqueue.exception.ResourceNotFoundException;
import com.bamako.fuelqueue.exception.UnauthorizedException;
import com.bamako.fuelqueue.repository.ConsumptionRecordRepository;
import com.bamako.fuelqueue.repository.StationRepository;
import com.bamako.fuelqueue.repository.TicketRepository;
import com.bamako.fuelqueue.repository.UserRepository;
import com.bamako.fuelqueue.repository.VehicleRepository;
import com.bamako.fuelqueue.security.SecurityUtils;
import com.bamako.fuelqueue.security.UserPrincipal;
import com.bamako.fuelqueue.service.TicketService;
import com.bamako.fuelqueue.util.QrCodeService;
import jakarta.transaction.Transactional;
import java.time.Instant;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
@Transactional
public class TicketServiceImpl implements TicketService {

    private static final String QUEUE_TOPIC_TEMPLATE = "/topic/stations/%s/queue";

    private final TicketRepository ticketRepository;
    private final StationRepository stationRepository;
    private final VehicleRepository vehicleRepository;
    private final UserRepository userRepository;
    private final ConsumptionRecordRepository consumptionRecordRepository;
    private final QrCodeService qrCodeService;
    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public TicketResponse createTicket(CreateTicketRequest request) {
        User user = getAuthenticatedUser();
        if (user.getRole() != UserRole.USAGER) {
            throw new ForbiddenException("Only customers can create tickets");
        }

        Station station = stationRepository.findById(request.getStationId())
                .orElseThrow(() -> new ResourceNotFoundException("Station not found"));
        Vehicle vehicle = vehicleRepository.findById(request.getVehicleId())
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found"));
        if (!vehicle.getUser().getId().equals(user.getId())) {
            throw new ForbiddenException("Vehicle does not belong to current user");
        }

        Set<TicketStatus> activeStatuses = EnumSet.of(TicketStatus.WAITING);
        if (ticketRepository.existsByStationIdAndUserIdAndStatusIn(station.getId(), user.getId(), activeStatuses)) {
            throw new BadRequestException("You already have a waiting ticket for this station");
        }

        int nextPosition = ticketRepository
                .findTopByStationIdAndFuelTypeAndStatusOrderByPositionDesc(station.getId(), request.getFuelType(), TicketStatus.WAITING)
                .map(ticket -> ticket.getPosition() + 1)
                .orElse(1);

        Ticket ticket = Ticket.builder()
                .ticketCode("TK-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase())
                .user(user)
                .vehicle(vehicle)
                .station(station)
                .fuelType(request.getFuelType())
                .requestedLiters(request.getRequestedLiters())
                .status(TicketStatus.WAITING)
                .position(nextPosition)
                .build();

        ticketRepository.save(ticket);
        String qrContent = buildQrContent(ticket.getId(), station.getId());
        String qrCode = qrCodeService.generateDataUri(qrContent);

        publishQueueUpdate(station.getId());
        return TicketMapper.toDto(ticket, qrCode);
    }

    @Override
    public TicketResponse getTicket(UUID ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found"));
        User user = getAuthenticatedUser();
        if (!canAccessTicket(user, ticket)) {
            throw new ForbiddenException("You are not allowed to view this ticket");
        }
        String qrCode = qrCodeService.generateDataUri(buildQrContent(ticket.getId(), ticket.getStation().getId()));
        return TicketMapper.toDto(ticket, qrCode);
    }

    @Override
    public void cancelTicket(UUID ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found"));
        User user = getAuthenticatedUser();
        if (!ticket.getUser().getId().equals(user.getId()) && user.getRole() == UserRole.USAGER) {
            throw new ForbiddenException("You cannot cancel this ticket");
        }
        if (ticket.getStatus() != TicketStatus.WAITING) {
            throw new BadRequestException("Only waiting tickets can be cancelled");
        }
        ticket.setStatus(TicketStatus.CANCELLED);
        ticketRepository.save(ticket);
        recalculatePositions(ticket.getStation().getId(), ticket.getFuelType());
        publishQueueUpdate(ticket.getStation().getId());
    }

    @Override
    public List<TicketSummaryDto> getStationTickets(UUID stationId) {
        Station station = stationRepository.findById(stationId)
                .orElseThrow(() -> new ResourceNotFoundException("Station not found"));
        return ticketRepository.findByStationIdAndStatusOrderByPositionAsc(station.getId(), TicketStatus.WAITING)
                .stream()
                .map(TicketMapper::toSummary)
                .collect(Collectors.toList());
    }

    @Override
    public TicketResponse validateTicket(UUID stationId, UUID ticketId, String qrContent) {
        if (!StringUtils.hasText(qrContent)) {
            throw new BadRequestException("QR content is required");
        }
        User validator = getAuthenticatedUser();
        if (validator.getRole() == UserRole.USAGER) {
            throw new ForbiddenException("Only managers or admins can validate tickets");
        }

        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found"));
        if (!ticket.getStation().getId().equals(stationId)) {
            throw new BadRequestException("Ticket does not belong to this station");
        }
        String expectedContent = buildQrContent(ticket.getId(), stationId);
        if (!expectedContent.equals(qrContent)) {
            throw new BadRequestException("QR code mismatch");
        }
        if (ticket.getStatus() != TicketStatus.WAITING) {
            throw new BadRequestException("Ticket already processed");
        }
        if (consumptionRecordRepository.findByTicketId(ticketId).isPresent()) {
            throw new BadRequestException("Ticket already validated");
        }

        ticket.setStatus(TicketStatus.SERVED);
        ticket.setServedAt(Instant.now());
        ticketRepository.save(ticket);

        ConsumptionRecord record = ConsumptionRecord.builder()
                .ticket(ticket)
                .station(ticket.getStation())
                .litersSupplied(ticket.getRequestedLiters())
                .validatedBy(validator)
                .confirmationTime(Instant.now())
                .build();
        consumptionRecordRepository.save(record);

        recalculatePositions(stationId, ticket.getFuelType());
        publishQueueUpdate(stationId);

        String qrCode = qrCodeService.generateDataUri(expectedContent);
        return TicketMapper.toDto(ticket, qrCode);
    }

    @Override
    public QueueUpdateMessage buildQueueSnapshot(UUID stationId) {
        List<TicketSummaryDto> tickets = ticketRepository.findByStationIdAndStatusOrderByPositionAsc(stationId, TicketStatus.WAITING)
                .stream()
                .map(TicketMapper::toSummary)
                .collect(Collectors.toList());
        return QueueUpdateMessage.builder()
                .stationId(stationId)
                .tickets(tickets)
                .build();
    }

    private void recalculatePositions(UUID stationId, FuelType fuelType) {
        List<Ticket> waitingTickets = ticketRepository
                .findByStationIdAndFuelTypeAndStatusOrderByPositionAsc(stationId, fuelType, TicketStatus.WAITING);
        int position = 1;
        for (Ticket waitingTicket : waitingTickets) {
            waitingTicket.setPosition(position++);
        }
        ticketRepository.saveAll(waitingTickets);
    }

    private void publishQueueUpdate(UUID stationId) {
        QueueUpdateMessage message = buildQueueSnapshot(stationId);
        messagingTemplate.convertAndSend(String.format(QUEUE_TOPIC_TEMPLATE, stationId), message);
    }

    private User getAuthenticatedUser() {
        UserPrincipal principal = SecurityUtils.getCurrentUser()
                .orElseThrow(() -> new UnauthorizedException("You must be authenticated"));
        User user = userRepository.findById(principal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return user;
    }

    private boolean canAccessTicket(User user, Ticket ticket) {
        if (user.getRole() == UserRole.ADMIN || user.getRole() == UserRole.GERANT) {
            return true;
        }
        return ticket.getUser().getId().equals(user.getId());
    }

    private String buildQrContent(UUID ticketId, UUID stationId) {
        return ticketId + ":" + stationId;
    }
}
