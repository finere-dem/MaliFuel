package com.bamako.fuelqueue.domain.dto.common;

import java.util.UUID;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class LocalityDto {
    UUID id;
    String name;
    String region;
}
