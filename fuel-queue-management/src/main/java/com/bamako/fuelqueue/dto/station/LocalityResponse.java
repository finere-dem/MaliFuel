package com.bamako.fuelqueue.dto.station;

import lombok.Builder;
import lombok.Value;

import java.time.Instant;
import java.util.UUID;

@Value
@Builder
public class LocalityResponse {
    UUID id;
    String name;
    String region;
    Instant createdAt;
}
