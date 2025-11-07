package com.bamako.fuelqueue.repository;

import com.bamako.fuelqueue.domain.entity.Ticket;
import com.bamako.fuelqueue.domain.enums.FuelType;
import com.bamako.fuelqueue.domain.enums.TicketStatus;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.QueryHints;
import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;

public interface TicketRepository extends JpaRepository<Ticket, UUID> {

    Optional<Ticket> findByTicketCode(String ticketCode);

    Optional<Ticket> findByIdAndUserId(UUID ticketId, UUID userId);

    boolean existsByStationIdAndUserIdAndStatusIn(UUID stationId, UUID userId, Collection<TicketStatus> statuses);

    List<Ticket> findByUserId(UUID userId);

    List<Ticket> findByStationIdAndStatusOrderByPositionAsc(UUID stationId, TicketStatus status);

    List<Ticket> findByStationIdAndFuelTypeAndStatusOrderByPositionAsc(UUID stationId, FuelType fuelType, TicketStatus status);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints({@QueryHint(name = "jakarta.persistence.lock.timeout", value = "3000")})
    Optional<Ticket> findTopByStationIdAndFuelTypeAndStatusOrderByPositionDesc(UUID stationId, FuelType fuelType, TicketStatus status);
}
