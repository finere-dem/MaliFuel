package com.bamako.fuelqueue.domain.mapper;

import com.bamako.fuelqueue.domain.dto.common.VehicleDto;
import com.bamako.fuelqueue.domain.dto.auth.RegisterRequest;
import com.bamako.fuelqueue.domain.entity.User;
import com.bamako.fuelqueue.domain.entity.Vehicle;

public final class VehicleMapper {

    private VehicleMapper() {
    }

    public static VehicleDto toDto(Vehicle vehicle) {
        if (vehicle == null) {
            return null;
        }
        return VehicleDto.builder()
                .id(vehicle.getId())
                .plateNumber(vehicle.getPlateNumber())
                .make(vehicle.getMake())
                .model(vehicle.getModel())
                .fuelType(vehicle.getFuelType())
                .build();
    }

    public static Vehicle fromRegisterPayload(RegisterRequest.VehiclePayload payload, User owner) {
        return Vehicle.builder()
                .user(owner)
                .plateNumber(payload.getPlateNumber())
                .make(payload.getMake())
                .model(payload.getModel())
                .fuelType(payload.getFuelType())
                .build();
    }
}
