package com.bamako.fuelqueue.domain.mapper;

import com.bamako.fuelqueue.domain.dto.common.LocalityDto;
import com.bamako.fuelqueue.domain.dto.common.UserLocalityDto;
import com.bamako.fuelqueue.domain.dto.user.UserProfileDto;
import com.bamako.fuelqueue.domain.entity.User;
import com.bamako.fuelqueue.domain.entity.UserLocality;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public final class UserMapper {

    private UserMapper() {
    }

    public static UserProfileDto toProfile(User user) {
        if (user == null) {
            return null;
        }
        return UserProfileDto.builder()
                .id(user.getId())
                .phone(user.getPhone())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .vehicle(VehicleMapper.toDto(user.getVehicle()))
                .localities(toUserLocalityDtos(user.getLocalities().stream().toList()))
                .build();
    }

    private static List<UserLocalityDto> toUserLocalityDtos(List<UserLocality> userLocalities) {
        return userLocalities == null ? List.of() : userLocalities.stream()
                .filter(Objects::nonNull)
                .sorted(Comparator.comparing(UserLocality::isPrimary).reversed())
                .map(UserMapper::toUserLocalityDto)
                .collect(Collectors.toList());
    }

    private static UserLocalityDto toUserLocalityDto(UserLocality userLocality) {
        LocalityDto localityDto = LocalityMapper.toDto(userLocality.getLocality());
        return UserLocalityDto.builder()
                .id(userLocality.getId())
                .locality(localityDto)
                .primary(userLocality.isPrimary())
                .build();
    }
}
