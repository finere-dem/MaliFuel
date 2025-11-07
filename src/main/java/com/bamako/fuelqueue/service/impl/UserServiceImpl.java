package com.bamako.fuelqueue.service.impl;

import com.bamako.fuelqueue.domain.dto.user.AddUserLocalityRequest;
import com.bamako.fuelqueue.domain.dto.user.UpdateUserProfileRequest;
import com.bamako.fuelqueue.domain.dto.user.UserProfileDto;
import com.bamako.fuelqueue.domain.entity.Locality;
import com.bamako.fuelqueue.domain.entity.User;
import com.bamako.fuelqueue.domain.entity.UserLocality;
import com.bamako.fuelqueue.domain.mapper.UserMapper;
import com.bamako.fuelqueue.exception.BadRequestException;
import com.bamako.fuelqueue.exception.ResourceNotFoundException;
import com.bamako.fuelqueue.exception.UnauthorizedException;
import com.bamako.fuelqueue.repository.LocalityRepository;
import com.bamako.fuelqueue.repository.UserLocalityRepository;
import com.bamako.fuelqueue.repository.UserRepository;
import com.bamako.fuelqueue.repository.VehicleRepository;
import com.bamako.fuelqueue.security.SecurityUtils;
import com.bamako.fuelqueue.security.UserPrincipal;
import com.bamako.fuelqueue.service.UserService;
import jakarta.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final VehicleRepository vehicleRepository;
    private final LocalityRepository localityRepository;
    private final UserLocalityRepository userLocalityRepository;

    @Override
    public UserProfileDto getCurrentUserProfile() {
        User user = loadCurrentUser();
        return UserMapper.toProfile(user);
    }

    @Override
    public UserProfileDto updateCurrentUser(UpdateUserProfileRequest request) {
        User user = loadCurrentUser();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        userRepository.save(user);
        return UserMapper.toProfile(user);
    }

    @Override
    public UserProfileDto addLocalityToCurrentUser(AddUserLocalityRequest request) {
        User user = loadCurrentUser();
        Locality locality = localityRepository.findById(request.getLocalityId())
                .orElseThrow(() -> new ResourceNotFoundException("Locality not found"));
        if (userLocalityRepository.existsByUserAndLocality(user, locality)) {
            throw new BadRequestException("Locality already assigned to user");
        }

        List<UserLocality> existing = userLocalityRepository.findByUser(user);
        if (existing.size() >= 2) {
            throw new BadRequestException("Maximum of 2 localities allowed");
        }

        if (request.isPrimary()) {
            existing.stream().filter(UserLocality::isPrimary).forEach(ul -> ul.setPrimary(false));
        }

        UserLocality userLocality = UserLocality.builder()
                .user(user)
                .locality(locality)
                .isPrimary(request.isPrimary())
                .build();
        userLocalityRepository.save(userLocality);

        existing.add(userLocality);
        user.setLocalities(new HashSet<>(existing));
        return UserMapper.toProfile(user);
    }

    private User loadCurrentUser() {
        UserPrincipal principal = SecurityUtils.getCurrentUser()
                .orElseThrow(() -> new UnauthorizedException("You must be authenticated"));
        User user = userRepository.findById(principal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user.setVehicle(vehicleRepository.findByUser(user).orElse(null));
        user.setLocalities(new HashSet<>(userLocalityRepository.findByUser(user)));
        return user;
    }
}
