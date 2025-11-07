package com.bamako.fuelqueue.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
public class StationAnalyticsResponse {
    private UUID stationId;
    private String stationName;
    private long waitingTickets;
    private long servedTickets;
    private long totalLitersDistributed;
}
