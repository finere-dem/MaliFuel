package com.bamako.fuelqueue.service.impl;

import com.bamako.fuelqueue.domain.entity.Station;
import com.bamako.fuelqueue.domain.enumeration.TicketStatus;
import com.bamako.fuelqueue.domain.repository.ConsumptionRecordRepository;
import com.bamako.fuelqueue.domain.repository.StationRepository;
import com.bamako.fuelqueue.domain.repository.TicketRepository;
import com.bamako.fuelqueue.dto.response.AnalyticsResponse;
import com.bamako.fuelqueue.dto.response.StationAnalyticsResponse;
import com.bamako.fuelqueue.exception.BadRequestException;
import com.bamako.fuelqueue.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AnalyticsServiceImpl implements AnalyticsService {

    private final ConsumptionRecordRepository consumptionRecordRepository;
    private final TicketRepository ticketRepository;
    private final StationRepository stationRepository;

    @Override
    public AnalyticsResponse getConsumptionAnalytics(OffsetDateTime from, OffsetDateTime to) {
        OffsetDateTime end = to != null ? to : OffsetDateTime.now();
        OffsetDateTime start = from != null ? from : end.minusDays(30);

        if (start.isAfter(end)) {
            throw new BadRequestException("From date must be before to date.");
        }

        long totalLiters = consumptionRecordRepository.sumLitersBetween(start, end);
        long ticketsServed = consumptionRecordRepository.countTicketsServedBetween(start, end);
        long waitingTickets = ticketRepository.countByStatus(TicketStatus.WAITING);
        long cancelledTickets = ticketRepository.countByStatus(TicketStatus.CANCELLED);

        List<StationAnalyticsResponse> stationSnapshots = stationRepository.findAll().stream()
            .map(station -> buildStationSnapshot(station, start, end))
            .collect(Collectors.toList());

        return AnalyticsResponse.builder()
            .totalLitersDistributed(totalLiters)
            .ticketsServed(ticketsServed)
            .waitingTickets(waitingTickets)
            .cancelledTickets(cancelledTickets)
            .stationSnapshots(stationSnapshots)
            .build();
    }

    private StationAnalyticsResponse buildStationSnapshot(Station station, OffsetDateTime from, OffsetDateTime to) {
        long served = consumptionRecordRepository.countTicketsServedByStationBetween(station.getId(), from, to);
        long liters = consumptionRecordRepository.sumLitersByStationBetween(station.getId(), from, to);
        long waiting = ticketRepository.countByStationIdAndStatus(station.getId(), TicketStatus.WAITING);
        return StationAnalyticsResponse.builder()
            .stationId(station.getId())
            .stationName(station.getName())
            .servedTickets(served)
            .totalLitersDistributed(liters)
            .waitingTickets(waiting)
            .build();
    }
}
