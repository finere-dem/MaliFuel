package com.bamako.fuelqueue.domain.repository;

import com.bamako.fuelqueue.domain.entity.Ticket;
import com.bamako.fuelqueue.domain.enumeration.FuelType;
import com.bamako.fuelqueue.domain.enumeration.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TicketRepository extends JpaRepository<Ticket, UUID> {

    Optional<Ticket> findByTicketCode(String ticketCode);

    Optional<Ticket> findByIdAndUserId(UUID ticketId, UUID userId);

    boolean existsByUserIdAndStatusIn(UUID userId, List<TicketStatus> statuses);

    long countByStationIdAndFuelTypeAndStatus(UUID stationId, FuelType fuelType, TicketStatus status);

    List<Ticket> findByStationIdAndFuelTypeAndStatusOrderByPositionAsc(UUID stationId, FuelType fuelType, TicketStatus status);

    List<Ticket> findByStationIdAndStatusOrderByPositionAsc(UUID stationId, TicketStatus status);

    long countByStatus(TicketStatus status);

    long countByStationIdAndStatus(UUID stationId, TicketStatus status);
}
