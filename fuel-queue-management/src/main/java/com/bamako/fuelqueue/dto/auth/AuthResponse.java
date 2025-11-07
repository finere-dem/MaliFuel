package com.bamako.fuelqueue.dto.auth;

import com.bamako.fuelqueue.enums.Role;
import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class AuthResponse {
    String accessToken;
    String tokenType;
    long expiresIn;
    UUID userId;
    Role role;
}
