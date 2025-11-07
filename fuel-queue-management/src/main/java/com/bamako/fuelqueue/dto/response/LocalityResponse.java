package com.bamako.fuelqueue.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
public class LocalityResponse {
    private UUID id;
    private String name;
    private String region;
    private OffsetDateTime createdAt;
}
