package com.bamako.fuelqueue.domain.mapper;

import com.bamako.fuelqueue.domain.dto.common.LocalityDto;
import com.bamako.fuelqueue.domain.entity.Locality;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public final class LocalityMapper {

    private LocalityMapper() {
    }

    public static LocalityDto toDto(Locality locality) {
        if (locality == null) {
            return null;
        }
        return LocalityDto.builder()
                .id(locality.getId())
                .name(locality.getName())
                .region(locality.getRegion())
                .build();
    }

    public static List<LocalityDto> toDtoList(List<Locality> localities) {
        return localities == null ? List.of() : localities.stream()
                .filter(Objects::nonNull)
                .map(LocalityMapper::toDto)
                .collect(Collectors.toList());
    }
}
