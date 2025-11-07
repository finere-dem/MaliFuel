package com.bamako.fuelqueue.domain.dto.station;

import com.bamako.fuelqueue.domain.dto.common.LocalityDto;
import com.bamako.fuelqueue.domain.enums.FuelType;
import java.util.Map;
import java.util.UUID;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class StationResponse {
    UUID id;
    String name;
    LocalityDto locality;
    Double latitude;
    Double longitude;
    boolean ravitailed;
    Map<FuelType, Integer> maxCapacityPerFuel;
}
