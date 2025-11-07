package com.bamako.fuelqueue.repository;

import com.bamako.fuelqueue.entity.Ticket;
import com.bamako.fuelqueue.enums.FuelType;
import com.bamako.fuelqueue.enums.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TicketRepository extends JpaRepository<Ticket, UUID> {

    Optional<Ticket> findByTicketCode(String ticketCode);

    Optional<Ticket> findByIdAndUserId(UUID ticketId, UUID userId);

    Optional<Ticket> findByIdAndStationId(UUID ticketId, UUID stationId);

    List<Ticket> findByUserIdOrderByCreatedAtDesc(UUID userId);

    List<Ticket> findByStationIdAndFuelTypeAndStatusOrderByPositionAsc(UUID stationId, FuelType fuelType, TicketStatus status);

    List<Ticket> findByStationIdAndStatusInOrderByPositionAsc(UUID stationId, Collection<TicketStatus> statuses);

    boolean existsByUserIdAndStatus(UUID userId, TicketStatus status);

    int countByStationIdAndFuelTypeAndStatus(UUID stationId, FuelType fuelType, TicketStatus status);
}
