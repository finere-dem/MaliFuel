package com.bamako.fuelqueue.mapper;

import com.bamako.fuelqueue.dto.user.UserLocalityResponse;
import com.bamako.fuelqueue.dto.user.UserResponse;
import com.bamako.fuelqueue.dto.user.VehicleResponse;
import com.bamako.fuelqueue.entity.User;
import com.bamako.fuelqueue.entity.UserLocality;
import com.bamako.fuelqueue.entity.Vehicle;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

@Component
public class UserMapper {

    public UserResponse toResponse(User user) {
        if (user == null) {
            return null;
        }
        return UserResponse.builder()
                .id(user.getId())
                .phone(user.getPhone())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole())
                .vehicle(toVehicleResponse(user.getVehicle()))
                .localities(toUserLocalityResponses(user))
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    private List<UserLocalityResponse> toUserLocalityResponses(User user) {
        if (user.getLocalities() == null) {
            return List.of();
        }
        return user.getLocalities().stream()
                .sorted(Comparator.comparing(UserLocality::isPrimary).reversed())
                .map(this::toUserLocalityResponse)
                .toList();
    }

    private UserLocalityResponse toUserLocalityResponse(UserLocality userLocality) {
        return UserLocalityResponse.builder()
                .id(userLocality.getId())
                .localityId(userLocality.getLocality().getId())
                .localityName(userLocality.getLocality().getName())
                .region(userLocality.getLocality().getRegion())
                .primary(userLocality.isPrimary())
                .build();
    }

    public VehicleResponse toVehicleResponse(Vehicle vehicle) {
        if (vehicle == null) {
            return null;
        }
        return VehicleResponse.builder()
                .id(vehicle.getId())
                .plateNumber(vehicle.getPlateNumber())
                .make(vehicle.getMake())
                .model(vehicle.getModel())
                .fuelType(vehicle.getFuelType())
                .build();
    }
}
