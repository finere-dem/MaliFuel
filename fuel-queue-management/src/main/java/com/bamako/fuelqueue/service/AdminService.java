package com.bamako.fuelqueue.service;

import com.bamako.fuelqueue.domain.enumeration.UserRole;
import com.bamako.fuelqueue.dto.response.UserResponse;

import java.util.List;
import java.util.UUID;

public interface AdminService {

    List<UserResponse> listUsers();

    UserResponse getUser(UUID userId);

    UserResponse updateUserRole(UUID userId, UserRole role);

    void deleteUser(UUID userId);
}
