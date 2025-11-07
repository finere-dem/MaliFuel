package com.bamako.fuelqueue.domain.mapper;

import com.bamako.fuelqueue.domain.entity.Locality;
import com.bamako.fuelqueue.dto.request.LocalityRequest;
import com.bamako.fuelqueue.dto.response.LocalityResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface LocalityMapper {

    LocalityResponse toDto(Locality locality);

    Locality toEntity(LocalityRequest request);

    void updateEntity(LocalityRequest request, @MappingTarget Locality locality);
}
