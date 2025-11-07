package com.bamako.fuelqueue.service;

import com.bamako.fuelqueue.dto.user.AddLocalityRequest;
import com.bamako.fuelqueue.dto.user.UpdateUserRequest;
import com.bamako.fuelqueue.dto.user.UserResponse;
import com.bamako.fuelqueue.entity.Locality;
import com.bamako.fuelqueue.entity.User;
import com.bamako.fuelqueue.entity.UserLocality;
import com.bamako.fuelqueue.exception.BadRequestException;
import com.bamako.fuelqueue.exception.ResourceNotFoundException;
import com.bamako.fuelqueue.mapper.UserMapper;
import com.bamako.fuelqueue.repository.LocalityRepository;
import com.bamako.fuelqueue.repository.UserLocalityRepository;
import com.bamako.fuelqueue.repository.UserRepository;
import com.bamako.fuelqueue.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final LocalityRepository localityRepository;
    private final UserLocalityRepository userLocalityRepository;
    private final UserMapper userMapper;

    @Transactional(readOnly = true)
    public UserResponse getCurrentUserProfile() {
        User user = getAuthenticatedUser();
        return userMapper.toResponse(user);
    }

    @Transactional
    public UserResponse updateCurrentUser(UpdateUserRequest request) {
        User user = getAuthenticatedUser();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        userRepository.save(user);
        return userMapper.toResponse(user);
    }

    @Transactional
    public UserResponse addLocality(AddLocalityRequest request) {
        User user = getAuthenticatedUser();

        long localityCount = userLocalityRepository.countByUserId(user.getId());
        if (localityCount >= 2) {
            throw new BadRequestException("Maximum number of localities reached");
        }

        if (user.getLocalities().stream().anyMatch(l -> l.getLocality().getId().equals(request.getLocalityId()))) {
            throw new BadRequestException("Locality is already associated to the user");
        }

        Locality locality = localityRepository.findById(request.getLocalityId())
                .orElseThrow(() -> new ResourceNotFoundException("Locality not found"));

        if (request.isPrimary()) {
            user.getLocalities().forEach(existing -> existing.setPrimary(false));
            userLocalityRepository.saveAll(user.getLocalities());
        } else if (userLocalityRepository.findByUserIdAndPrimaryTrue(user.getId()).isEmpty()) {
            request.setPrimary(true);
        }

        UserLocality userLocality = UserLocality.builder()
                .user(user)
                .locality(locality)
                .primary(request.isPrimary())
                .build();
        userLocalityRepository.save(userLocality);
        user.getLocalities().add(userLocality);

        return userMapper.toResponse(user);
    }

    private User getAuthenticatedUser() {
        UUID currentUserId = SecurityUtils.getCurrentUserId();
        if (currentUserId == null) {
            throw new BadRequestException("No authenticated user found");
        }
        return userRepository.findById(currentUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Authenticated user not found"));
    }
}
