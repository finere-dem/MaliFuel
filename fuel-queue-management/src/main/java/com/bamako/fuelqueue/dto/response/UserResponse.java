package com.bamako.fuelqueue.dto.response;

import com.bamako.fuelqueue.domain.enumeration.UserRole;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
public class UserResponse {
    private UUID id;
    private String phone;
    private String firstName;
    private String lastName;
    private UserRole role;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private VehicleResponse vehicle;
    private List<UserLocalityResponse> localities;
}
