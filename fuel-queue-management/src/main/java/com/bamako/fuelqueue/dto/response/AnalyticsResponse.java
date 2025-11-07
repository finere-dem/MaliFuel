package com.bamako.fuelqueue.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class AnalyticsResponse {
    private long ticketsServed;
    private long waitingTickets;
    private long cancelledTickets;
    private long totalLitersDistributed;
    private List<StationAnalyticsResponse> stationSnapshots;
}
