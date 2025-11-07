package com.bamako.fuelqueue.service;

import com.bamako.fuelqueue.dto.request.AuthLoginRequest;
import com.bamako.fuelqueue.dto.request.AuthRegisterRequest;
import com.bamako.fuelqueue.dto.response.AuthResponse;

public interface AuthService {

    AuthResponse register(AuthRegisterRequest request);

    AuthResponse login(AuthLoginRequest request);
}
