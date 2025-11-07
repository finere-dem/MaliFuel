package com.bamako.fuelqueue.domain.mapper;

import com.bamako.fuelqueue.domain.entity.Vehicle;
import com.bamako.fuelqueue.dto.request.VehicleRegistrationRequest;
import com.bamako.fuelqueue.dto.request.VehicleUpdateRequest;
import com.bamako.fuelqueue.dto.response.VehicleResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface VehicleMapper {

    VehicleResponse toDto(Vehicle vehicle);

    Vehicle toEntity(VehicleRegistrationRequest request);

    void updateEntity(VehicleUpdateRequest request, @MappingTarget Vehicle vehicle);
}
