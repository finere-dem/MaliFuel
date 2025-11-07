package com.bamako.fuelqueue.dto.user;

import com.bamako.fuelqueue.enums.Role;
import lombok.Builder;
import lombok.Value;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Value
@Builder
public class UserResponse {
    UUID id;
    String phone;
    String firstName;
    String lastName;
    Role role;
    VehicleResponse vehicle;
    List<UserLocalityResponse> localities;
    Instant createdAt;
    Instant updatedAt;
}
