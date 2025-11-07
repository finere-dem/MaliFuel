package com.bamako.fuelqueue.domain.dto.auth;

import com.bamako.fuelqueue.domain.dto.user.UserProfileDto;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class AuthResponse {
    String tokenType;
    String accessToken;
    long expiresIn;
    UserProfileDto user;
}
