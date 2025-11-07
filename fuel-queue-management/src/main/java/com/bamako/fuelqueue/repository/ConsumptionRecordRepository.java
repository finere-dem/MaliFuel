package com.bamako.fuelqueue.repository;

import com.bamako.fuelqueue.entity.ConsumptionRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ConsumptionRecordRepository extends JpaRepository<ConsumptionRecord, UUID> {

    Optional<ConsumptionRecord> findByTicketId(UUID ticketId);

    List<ConsumptionRecord> findByConfirmationTimeBetween(Instant from, Instant to);
}
