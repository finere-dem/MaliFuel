package com.bamako.fuelqueue.service;

import com.bamako.fuelqueue.dto.auth.AuthResponse;
import com.bamako.fuelqueue.dto.auth.LoginRequest;
import com.bamako.fuelqueue.dto.auth.RegisterRequest;
import com.bamako.fuelqueue.entity.Locality;
import com.bamako.fuelqueue.entity.User;
import com.bamako.fuelqueue.entity.UserLocality;
import com.bamako.fuelqueue.entity.Vehicle;
import com.bamako.fuelqueue.enums.Role;
import com.bamako.fuelqueue.exception.BadRequestException;
import com.bamako.fuelqueue.exception.ResourceNotFoundException;
import com.bamako.fuelqueue.repository.LocalityRepository;
import com.bamako.fuelqueue.repository.UserLocalityRepository;
import com.bamako.fuelqueue.repository.UserRepository;
import com.bamako.fuelqueue.repository.VehicleRepository;
import com.bamako.fuelqueue.security.UserPrincipal;
import com.bamako.fuelqueue.security.jwt.JwtProperties;
import com.bamako.fuelqueue.security.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final VehicleRepository vehicleRepository;
    private final LocalityRepository localityRepository;
    private final UserLocalityRepository userLocalityRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final JwtProperties jwtProperties;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        validateRegisterRequest(request);

        if (userRepository.existsByPhone(request.getPhone())) {
            throw new BadRequestException("Phone number already registered");
        }

        if (vehicleRepository.existsByPlateNumber(request.getVehicle().getPlateNumber())) {
            throw new BadRequestException("Vehicle with the same plate number already exists");
        }

        User user = userRepository.save(User.builder()
                .phone(request.getPhone())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USAGER)
                .build());

        Vehicle vehicle = Vehicle.builder()
                .user(user)
                .plateNumber(request.getVehicle().getPlateNumber())
                .make(request.getVehicle().getMake())
                .model(request.getVehicle().getModel())
                .fuelType(request.getVehicle().getFuelType())
                .build();
        vehicleRepository.save(vehicle);
        user.setVehicle(vehicle);

        List<UserLocality> userLocalities = request.getLocalities().stream()
                .map(localityRequest -> UserLocality.builder()
                        .user(user)
                        .locality(resolveLocality(localityRequest.getLocalityId()))
                        .primary(localityRequest.isPrimary())
                        .build())
                .collect(Collectors.toList());

        userLocalityRepository.saveAll(userLocalities);
        user.getLocalities().addAll(userLocalities);

        return buildAuthResponse(user);
    }

    public AuthResponse authenticate(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getPhone(), request.getPassword()));
        User user = userRepository.findByPhone(request.getPhone())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return buildAuthResponse(user);
    }

    private AuthResponse buildAuthResponse(User user) {
        UserPrincipal principal = UserPrincipal.fromUser(user);
        String token = jwtService.generateToken(principal);

        return AuthResponse.builder()
                .accessToken(token)
                .tokenType("Bearer")
                .expiresIn(jwtProperties.getExpiration())
                .userId(user.getId())
                .role(user.getRole())
                .build();
    }

    private Locality resolveLocality(UUID localityId) {
        return localityRepository.findById(localityId)
                .orElseThrow(() -> new ResourceNotFoundException("Locality not found: " + localityId));
    }

    private void validateRegisterRequest(RegisterRequest request) {
        if (request.getLocalities() == null || request.getLocalities().isEmpty()) {
            throw new BadRequestException("At least one locality must be provided");
        }

        long primaryCount = request.getLocalities().stream()
                .filter(RegisterRequest.UserLocalityRequest::isPrimary)
                .count();

        if (primaryCount == 0) {
            // Ensure the first locality is assigned as primary when none specified
            request.getLocalities().get(0).setPrimary(true);
        } else if (primaryCount > 1) {
            throw new BadRequestException("Only one locality can be marked as primary");
        }

        if (request.getLocalities().stream().map(RegisterRequest.UserLocalityRequest::getLocalityId).distinct().count()
                != request.getLocalities().size()) {
            throw new BadRequestException("Duplicate localities are not allowed");
        }
    }
}
