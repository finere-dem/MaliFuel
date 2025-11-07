package com.bamako.fuelqueue.repository;

import com.bamako.fuelqueue.domain.entity.ConsumptionRecord;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ConsumptionRecordRepository extends JpaRepository<ConsumptionRecord, UUID> {

    Optional<ConsumptionRecord> findByTicketId(UUID ticketId);

    @Query("SELECT COUNT(cr) FROM ConsumptionRecord cr WHERE cr.confirmationTime BETWEEN :from AND :to")
    long countServedTicketsBetween(@Param("from") Instant from, @Param("to") Instant to);

    @Query("SELECT COALESCE(SUM(cr.litersSupplied), 0) FROM ConsumptionRecord cr WHERE cr.confirmationTime BETWEEN :from AND :to")
    long sumLitersSuppliedBetween(@Param("from") Instant from, @Param("to") Instant to);

    @Query("SELECT cr FROM ConsumptionRecord cr WHERE cr.station.id = :stationId")
    List<ConsumptionRecord> findByStationId(@Param("stationId") UUID stationId);
}
