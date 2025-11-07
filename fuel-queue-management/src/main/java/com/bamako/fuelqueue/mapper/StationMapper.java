package com.bamako.fuelqueue.mapper;

import com.bamako.fuelqueue.dto.station.StationResponse;
import com.bamako.fuelqueue.dto.station.StationSummary;
import com.bamako.fuelqueue.entity.Station;
import org.springframework.stereotype.Component;

@Component
public class StationMapper {

    public StationResponse toResponse(Station station) {
        if (station == null) {
            return null;
        }
        return StationResponse.builder()
                .id(station.getId())
                .name(station.getName())
                .localityId(station.getLocality().getId())
                .localityName(station.getLocality().getName())
                .region(station.getLocality().getRegion())
                .latitude(station.getLatitude())
                .longitude(station.getLongitude())
                .ravitailed(station.isRavitailed())
                .maxCapacityPerFuel(station.getMaxCapacityPerFuel())
                .createdAt(station.getCreatedAt())
                .updatedAt(station.getUpdatedAt())
                .build();
    }

    public StationSummary toSummary(Station station) {
        if (station == null) {
            return null;
        }
        return StationSummary.builder()
                .id(station.getId())
                .name(station.getName())
                .localityId(station.getLocality().getId())
                .localityName(station.getLocality().getName())
                .build();
    }
}
