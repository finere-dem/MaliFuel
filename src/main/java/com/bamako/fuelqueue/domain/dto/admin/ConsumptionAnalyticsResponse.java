package com.bamako.fuelqueue.domain.dto.admin;

import java.time.Instant;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ConsumptionAnalyticsResponse {
    Instant from;
    Instant to;
    long ticketsServed;
    long litersSupplied;
}
