package com.bamako.fuelqueue.dto.user;

import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class UserLocalityResponse {
    UUID id;
    UUID localityId;
    String localityName;
    String region;
    boolean primary;
}
