package com.bamako.fuelqueue.domain.mapper;

import com.bamako.fuelqueue.domain.entity.Station;
import com.bamako.fuelqueue.dto.request.StationCreateRequest;
import com.bamako.fuelqueue.dto.request.StationUpdateRequest;
import com.bamako.fuelqueue.dto.response.StationResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {LocalityMapper.class})
public interface StationMapper {

    StationResponse toDto(Station station);

    Station toEntity(StationCreateRequest request);

    void updateEntity(StationUpdateRequest request, @MappingTarget Station station);
}
