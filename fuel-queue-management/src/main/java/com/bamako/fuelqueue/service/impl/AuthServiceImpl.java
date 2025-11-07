package com.bamako.fuelqueue.service.impl;

import com.bamako.fuelqueue.domain.entity.Locality;
import com.bamako.fuelqueue.domain.entity.User;
import com.bamako.fuelqueue.domain.entity.UserLocality;
import com.bamako.fuelqueue.domain.entity.Vehicle;
import com.bamako.fuelqueue.domain.enumeration.UserRole;
import com.bamako.fuelqueue.domain.mapper.UserMapper;
import com.bamako.fuelqueue.domain.mapper.VehicleMapper;
import com.bamako.fuelqueue.domain.repository.LocalityRepository;
import com.bamako.fuelqueue.domain.repository.UserRepository;
import com.bamako.fuelqueue.domain.repository.VehicleRepository;
import com.bamako.fuelqueue.dto.request.AuthLoginRequest;
import com.bamako.fuelqueue.dto.request.AuthRegisterRequest;
import com.bamako.fuelqueue.dto.request.UserLocalityRegistrationRequest;
import com.bamako.fuelqueue.dto.response.AuthResponse;
import com.bamako.fuelqueue.exception.BadRequestException;
import com.bamako.fuelqueue.exception.ConflictException;
import com.bamako.fuelqueue.exception.ResourceNotFoundException;
import com.bamako.fuelqueue.exception.UnauthorizedException;
import com.bamako.fuelqueue.security.jwt.JwtService;
import com.bamako.fuelqueue.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final VehicleRepository vehicleRepository;
    private final LocalityRepository localityRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserMapper userMapper;
    private final VehicleMapper vehicleMapper;

    @Override
    public AuthResponse register(AuthRegisterRequest request) {
        if (userRepository.existsByPhone(request.getPhone())) {
            throw new ConflictException("Phone number already registered.");
        }

        if (vehicleRepository.existsByPlateNumber(request.getVehicle().getPlateNumber())) {
            throw new ConflictException("Vehicle plate number already registered.");
        }

        if (request.getLocalities() != null && request.getLocalities().size() > 2) {
            throw new BadRequestException("Maximum of two localities are allowed.");
        }

        User user = User.builder()
            .phone(request.getPhone())
            .firstName(request.getFirstName())
            .lastName(request.getLastName())
            .password(passwordEncoder.encode(request.getPassword()))
            .role(UserRole.USAGER)
            .build();

        Vehicle vehicle = vehicleMapper.toEntity(request.getVehicle());
        vehicle.setUser(user);
        user.setVehicle(vehicle);

        Set<UserLocality> localityAssociations = new HashSet<>();
        boolean primarySet = false;
        List<UserLocalityRegistrationRequest> localityRequests = request.getLocalities();
        if (localityRequests != null && !localityRequests.isEmpty()) {
            for (UserLocalityRegistrationRequest localityRequest : localityRequests) {
                Locality locality = localityRepository.findById(localityRequest.getLocalityId())
                    .orElseThrow(() -> new ResourceNotFoundException("Locality not found."));
                boolean isPrimary = localityRequest.isPrimary();
                if (isPrimary) {
                    if (primarySet) {
                        throw new BadRequestException("Only one primary locality can be defined.");
                    }
                    primarySet = true;
                }
                UserLocality association = UserLocality.builder()
                    .user(user)
                    .locality(locality)
                    .primaryLocality(isPrimary)
                    .build();
                localityAssociations.add(association);
            }
        }

        if (!primarySet && !localityAssociations.isEmpty()) {
            // Set first locality as primary if none flagged
            localityAssociations.iterator().next().setPrimaryLocality(true);
        }

        user.setLocalities(localityAssociations);

        User savedUser = userRepository.save(user);

        String token = jwtService.generateToken(savedUser);
        return AuthResponse.builder()
            .token(token)
            .user(userMapper.toDto(savedUser))
            .build();
    }

    @Override
    @Transactional(readOnly = true)
    public AuthResponse login(AuthLoginRequest request) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getPhone(), request.getPassword()));
        } catch (BadCredentialsException e) {
            throw new UnauthorizedException("Invalid credentials provided.");
        }

        User user = userRepository.findByPhone(request.getPhone())
            .orElseThrow(() -> new UnauthorizedException("Invalid credentials provided."));

        String token = jwtService.generateToken(user);
        return AuthResponse.builder()
            .token(token)
            .user(userMapper.toDto(user))
            .build();
    }
}
