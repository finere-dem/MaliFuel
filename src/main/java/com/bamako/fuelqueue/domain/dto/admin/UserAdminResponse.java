package com.bamako.fuelqueue.domain.dto.admin;

import com.bamako.fuelqueue.domain.enums.UserRole;
import java.time.Instant;
import java.util.UUID;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UserAdminResponse {
    UUID id;
    String phone;
    String firstName;
    String lastName;
    UserRole role;
    Instant createdAt;
    Instant updatedAt;
}
