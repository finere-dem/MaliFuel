package com.bamako.fuelqueue.service.impl;

import com.bamako.fuelqueue.domain.entity.Locality;
import com.bamako.fuelqueue.domain.entity.User;
import com.bamako.fuelqueue.domain.entity.UserLocality;
import com.bamako.fuelqueue.domain.entity.Vehicle;
import com.bamako.fuelqueue.domain.mapper.UserMapper;
import com.bamako.fuelqueue.domain.mapper.VehicleMapper;
import com.bamako.fuelqueue.domain.repository.LocalityRepository;
import com.bamako.fuelqueue.domain.repository.UserLocalityRepository;
import com.bamako.fuelqueue.domain.repository.UserRepository;
import com.bamako.fuelqueue.domain.repository.VehicleRepository;
import com.bamako.fuelqueue.dto.request.UserLocalityRegistrationRequest;
import com.bamako.fuelqueue.dto.request.UserUpdateRequest;
import com.bamako.fuelqueue.dto.request.VehicleUpdateRequest;
import com.bamako.fuelqueue.dto.response.UserResponse;
import com.bamako.fuelqueue.exception.BadRequestException;
import com.bamako.fuelqueue.exception.ConflictException;
import com.bamako.fuelqueue.exception.ResourceNotFoundException;
import com.bamako.fuelqueue.security.service.CurrentUserService;
import com.bamako.fuelqueue.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final CurrentUserService currentUserService;
    private final UserRepository userRepository;
    private final VehicleRepository vehicleRepository;
    private final LocalityRepository localityRepository;
    private final UserLocalityRepository userLocalityRepository;
    private final UserMapper userMapper;
    private final VehicleMapper vehicleMapper;

    @Override
    @Transactional(readOnly = true)
    public UserResponse getCurrentUser() {
        User user = currentUserService.getCurrentUser();
        return userMapper.toDto(user);
    }

    @Override
    public UserResponse updateCurrentUser(UserUpdateRequest request) {
        User user = currentUserService.getCurrentUser();

        if (request.getFirstName() != null && !request.getFirstName().isBlank()) {
            user.setFirstName(request.getFirstName());
        }
        if (request.getLastName() != null && !request.getLastName().isBlank()) {
            user.setLastName(request.getLastName());
        }

        Vehicle vehicle = user.getVehicle();
        VehicleUpdateRequest vehicleUpdate = request.getVehicle();
        if (vehicleUpdate != null) {
            if (vehicle == null) {
                throw new BadRequestException("Vehicle does not exist for this user.");
            }
            if (vehicleUpdate.getPlateNumber() != null && !vehicleUpdate.getPlateNumber().equals(vehicle.getPlateNumber())) {
                if (vehicleRepository.existsByPlateNumber(vehicleUpdate.getPlateNumber())) {
                    throw new ConflictException("Vehicle plate number already registered.");
                }
            }
            vehicleMapper.updateEntity(vehicleUpdate, vehicle);
        }

        User saved = userRepository.save(user);
        return userMapper.toDto(saved);
    }

    @Override
    public UserResponse addLocalityToCurrentUser(UserLocalityRegistrationRequest request) {
        User user = currentUserService.getCurrentUser();
        Set<UserLocality> associations = user.getLocalities();

        if (associations.size() >= 2) {
            throw new BadRequestException("Maximum of two localities already assigned.");
        }

        UUID localityId = request.getLocalityId();
        Optional<UserLocality> existing = associations.stream()
            .filter(item -> item.getLocality().getId().equals(localityId))
            .findFirst();
        if (existing.isPresent()) {
            throw new ConflictException("Locality already assigned to user.");
        }

        Locality locality = localityRepository.findById(localityId)
            .orElseThrow(() -> new ResourceNotFoundException("Locality not found."));

        boolean isPrimary = request.isPrimary();

        if (isPrimary) {
            associations.forEach(assoc -> assoc.setPrimaryLocality(false));
        } else if (associations.stream().noneMatch(UserLocality::isPrimaryLocality)) {
            isPrimary = true; // ensure at least one primary
        }

        UserLocality newAssociation = UserLocality.builder()
            .user(user)
            .locality(locality)
            .primaryLocality(isPrimary)
            .build();
        associations.add(newAssociation);

        userLocalityRepository.save(newAssociation);

        return userMapper.toDto(user);
    }
}
