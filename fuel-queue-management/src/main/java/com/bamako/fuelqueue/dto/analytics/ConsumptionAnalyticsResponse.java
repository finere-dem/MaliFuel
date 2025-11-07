package com.bamako.fuelqueue.dto.analytics;

import lombok.Builder;
import lombok.Value;

import java.time.Instant;

@Value
@Builder
public class ConsumptionAnalyticsResponse {
    Instant from;
    Instant to;
    long ticketsServed;
    long litersDistributed;
    long uniqueUsersServed;
}
