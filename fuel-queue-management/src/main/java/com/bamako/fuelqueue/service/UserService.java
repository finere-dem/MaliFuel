package com.bamako.fuelqueue.service;

import com.bamako.fuelqueue.dto.request.UserLocalityRegistrationRequest;
import com.bamako.fuelqueue.dto.request.UserUpdateRequest;
import com.bamako.fuelqueue.dto.response.UserResponse;

public interface UserService {

    UserResponse getCurrentUser();

    UserResponse updateCurrentUser(UserUpdateRequest request);

    UserResponse addLocalityToCurrentUser(UserLocalityRegistrationRequest request);
}
