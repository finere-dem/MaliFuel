package com.bamako.fuelqueue.domain.dto.user;

import com.bamako.fuelqueue.domain.dto.common.UserLocalityDto;
import com.bamako.fuelqueue.domain.dto.common.VehicleDto;
import com.bamako.fuelqueue.domain.enums.UserRole;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UserProfileDto {
    UUID id;
    String phone;
    String firstName;
    String lastName;
    UserRole role;
    Instant createdAt;
    Instant updatedAt;
    VehicleDto vehicle;
    List<UserLocalityDto> localities;
}
