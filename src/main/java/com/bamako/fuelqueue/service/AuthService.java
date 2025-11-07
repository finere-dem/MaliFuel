package com.bamako.fuelqueue.service;

import com.bamako.fuelqueue.domain.dto.auth.AuthResponse;
import com.bamako.fuelqueue.domain.dto.auth.LoginRequest;
import com.bamako.fuelqueue.domain.dto.auth.RegisterRequest;

public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);
}
