package com.bamako.fuelqueue.service;

import com.bamako.fuelqueue.domain.dto.user.AddUserLocalityRequest;
import com.bamako.fuelqueue.domain.dto.user.UpdateUserProfileRequest;
import com.bamako.fuelqueue.domain.dto.user.UserProfileDto;

public interface UserService {

    UserProfileDto getCurrentUserProfile();

    UserProfileDto updateCurrentUser(UpdateUserProfileRequest request);

    UserProfileDto addLocalityToCurrentUser(AddUserLocalityRequest request);
}
