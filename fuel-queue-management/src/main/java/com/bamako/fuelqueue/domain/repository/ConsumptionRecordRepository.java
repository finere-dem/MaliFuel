package com.bamako.fuelqueue.domain.repository;

import com.bamako.fuelqueue.domain.entity.ConsumptionRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

public interface ConsumptionRecordRepository extends JpaRepository<ConsumptionRecord, UUID> {

    Optional<ConsumptionRecord> findByTicketId(UUID ticketId);

    @Query("SELECT COALESCE(SUM(c.litersSupplied), 0) FROM ConsumptionRecord c WHERE c.confirmationTime BETWEEN :from AND :to")
    long sumLitersBetween(@Param("from") OffsetDateTime from, @Param("to") OffsetDateTime to);

    @Query("SELECT COUNT(c) FROM ConsumptionRecord c WHERE c.confirmationTime BETWEEN :from AND :to")
    long countTicketsServedBetween(@Param("from") OffsetDateTime from, @Param("to") OffsetDateTime to);

    @Query("SELECT COALESCE(SUM(c.litersSupplied), 0) FROM ConsumptionRecord c WHERE c.station.id = :stationId AND c.confirmationTime BETWEEN :from AND :to")
    long sumLitersByStationBetween(@Param("stationId") UUID stationId,
                                   @Param("from") OffsetDateTime from,
                                   @Param("to") OffsetDateTime to);

    @Query("SELECT COUNT(c) FROM ConsumptionRecord c WHERE c.station.id = :stationId AND c.confirmationTime BETWEEN :from AND :to")
    long countTicketsServedByStationBetween(@Param("stationId") UUID stationId,
                                            @Param("from") OffsetDateTime from,
                                            @Param("to") OffsetDateTime to);
}
