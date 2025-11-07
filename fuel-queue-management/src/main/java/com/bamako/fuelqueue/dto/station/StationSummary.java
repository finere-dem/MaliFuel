package com.bamako.fuelqueue.dto.station;

import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class StationSummary {
    UUID id;
    String name;
    UUID localityId;
    String localityName;
}
