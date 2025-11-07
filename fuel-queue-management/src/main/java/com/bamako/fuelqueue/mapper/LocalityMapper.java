package com.bamako.fuelqueue.mapper;

import com.bamako.fuelqueue.dto.station.LocalityResponse;
import com.bamako.fuelqueue.entity.Locality;
import org.springframework.stereotype.Component;

@Component
public class LocalityMapper {

    public LocalityResponse toResponse(Locality locality) {
        if (locality == null) {
            return null;
        }
        return LocalityResponse.builder()
                .id(locality.getId())
                .name(locality.getName())
                .region(locality.getRegion())
                .createdAt(locality.getCreatedAt())
                .build();
    }
}
