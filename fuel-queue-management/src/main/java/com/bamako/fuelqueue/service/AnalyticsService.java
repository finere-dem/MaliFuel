package com.bamako.fuelqueue.service;

import com.bamako.fuelqueue.dto.analytics.ConsumptionAnalyticsResponse;
import com.bamako.fuelqueue.entity.ConsumptionRecord;
import com.bamako.fuelqueue.repository.ConsumptionRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final ConsumptionRecordRepository consumptionRecordRepository;

    @Transactional(readOnly = true)
    public ConsumptionAnalyticsResponse getConsumptionAnalytics(Instant from, Instant to) {
        Instant effectiveTo = to != null ? to : Instant.now();
        Instant effectiveFrom = from != null ? from : effectiveTo.minus(30, ChronoUnit.DAYS);

        List<ConsumptionRecord> records = consumptionRecordRepository.findByConfirmationTimeBetween(effectiveFrom, effectiveTo);

        long totalLiters = records.stream()
                .mapToLong(ConsumptionRecord::getLitersSupplied)
                .sum();

        Set<UUID> uniqueUsers = records.stream()
                .map(record -> record.getTicket().getUser().getId())
                .collect(Collectors.toSet());

        return ConsumptionAnalyticsResponse.builder()
                .from(effectiveFrom)
                .to(effectiveTo)
                .ticketsServed(records.size())
                .litersDistributed(totalLiters)
                .uniqueUsersServed(uniqueUsers.size())
                .build();
    }
}
