package com.bamako.fuelqueue.domain.mapper;

import com.bamako.fuelqueue.domain.dto.station.StationResponse;
import com.bamako.fuelqueue.domain.entity.Station;

public final class StationMapper {

    private StationMapper() {
    }

    public static StationResponse toDto(Station station) {
        if (station == null) {
            return null;
        }
        return StationResponse.builder()
                .id(station.getId())
                .name(station.getName())
                .locality(LocalityMapper.toDto(station.getLocality()))
                .latitude(station.getLatitude())
                .longitude(station.getLongitude())
                .ravitailed(station.isRavitailed())
                .maxCapacityPerFuel(station.getMaxCapacityPerFuel())
                .build();
    }
}
