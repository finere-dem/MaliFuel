package com.bamako.fuelqueue.service.impl;

import com.bamako.fuelqueue.domain.dto.auth.AuthResponse;
import com.bamako.fuelqueue.domain.dto.auth.LoginRequest;
import com.bamako.fuelqueue.domain.dto.auth.RegisterRequest;
import com.bamako.fuelqueue.domain.dto.user.UserProfileDto;
import com.bamako.fuelqueue.domain.entity.Locality;
import com.bamako.fuelqueue.domain.entity.User;
import com.bamako.fuelqueue.domain.entity.UserLocality;
import com.bamako.fuelqueue.domain.entity.Vehicle;
import com.bamako.fuelqueue.domain.enums.UserRole;
import com.bamako.fuelqueue.domain.mapper.UserMapper;
import com.bamako.fuelqueue.domain.mapper.VehicleMapper;
import com.bamako.fuelqueue.exception.BadRequestException;
import com.bamako.fuelqueue.exception.ResourceNotFoundException;
import com.bamako.fuelqueue.repository.LocalityRepository;
import com.bamako.fuelqueue.repository.UserLocalityRepository;
import com.bamako.fuelqueue.repository.UserRepository;
import com.bamako.fuelqueue.repository.VehicleRepository;
import com.bamako.fuelqueue.security.UserPrincipal;
import com.bamako.fuelqueue.security.jwt.JwtProperties;
import com.bamako.fuelqueue.security.jwt.JwtService;
import com.bamako.fuelqueue.service.AuthService;
import jakarta.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final VehicleRepository vehicleRepository;
    private final LocalityRepository localityRepository;
    private final UserLocalityRepository userLocalityRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final JwtProperties jwtProperties;

    @Override
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByPhone(request.getPhone())) {
            throw new BadRequestException("Phone number already registered");
        }
        if (vehicleRepository.existsByPlateNumber(request.getVehicle().getPlateNumber())) {
            throw new BadRequestException("Vehicle plate number already registered");
        }
        if (request.getLocalities().size() > 2) {
            throw new BadRequestException("You can only assign up to 2 localities");
        }

        Map<UUID, RegisterRequest.UserLocalityPayload> localityPayloadMap = request.getLocalities().stream()
                .collect(Collectors.toMap(RegisterRequest.UserLocalityPayload::getLocalityId, payload -> payload, (a, b) -> a));
        List<Locality> localities = localityRepository.findAllById(localityPayloadMap.keySet());
        if (localities.size() != localityPayloadMap.size()) {
            throw new ResourceNotFoundException("One or more localities not found");
        }

        boolean hasPrimary = request.getLocalities().stream().anyMatch(RegisterRequest.UserLocalityPayload::isPrimary);
        if (!hasPrimary) {
            throw new BadRequestException("At least one locality must be marked as primary");
        }

        User user = User.builder()
                .phone(request.getPhone())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(UserRole.USAGER)
                .build();

        userRepository.save(user);

        Vehicle vehicle = VehicleMapper.fromRegisterPayload(request.getVehicle(), user);
        vehicleRepository.save(vehicle);
        user.setVehicle(vehicle);

        List<UserLocality> userLocalities = localities.stream()
                .map(locality -> {
                    RegisterRequest.UserLocalityPayload payload = localityPayloadMap.get(locality.getId());
                    return UserLocality.builder()
                            .user(user)
                            .locality(locality)
                            .isPrimary(payload.isPrimary())
                            .build();
                })
                .toList();

        userLocalityRepository.saveAll(userLocalities);
        user.setLocalities(new HashSet<>(userLocalities));

        return buildAuthResponse(user);
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getPhone(), request.getPassword()));
            UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
            User user = userRepository.findById(principal.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));
            user.setVehicle(vehicleRepository.findByUser(user).orElse(null));
            user.setLocalities(new HashSet<>(userLocalityRepository.findByUser(user)));
            return buildAuthResponse(user);
        } catch (AuthenticationException ex) {
            throw new BadRequestException("Invalid credentials");
        }
    }

    private AuthResponse buildAuthResponse(User user) {
        UserPrincipal principal = UserPrincipal.from(user);
        String token = jwtService.generateToken(principal);
        UserProfileDto profileDto = UserMapper.toProfile(user);
        return AuthResponse.builder()
                .tokenType("Bearer")
                .accessToken(token)
                .expiresIn(jwtProperties.getExpiration())
                .user(profileDto)
                .build();
    }
}
