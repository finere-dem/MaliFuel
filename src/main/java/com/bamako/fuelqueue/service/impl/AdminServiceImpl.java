package com.bamako.fuelqueue.service.impl;

import com.bamako.fuelqueue.domain.dto.admin.ConsumptionAnalyticsResponse;
import com.bamako.fuelqueue.domain.dto.admin.UserAdminResponse;
import com.bamako.fuelqueue.domain.entity.User;
import com.bamako.fuelqueue.domain.enums.UserRole;
import com.bamako.fuelqueue.exception.BadRequestException;
import com.bamako.fuelqueue.exception.ResourceNotFoundException;
import com.bamako.fuelqueue.repository.ConsumptionRecordRepository;
import com.bamako.fuelqueue.repository.UserRepository;
import com.bamako.fuelqueue.service.AdminService;
import jakarta.transaction.Transactional;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final ConsumptionRecordRepository consumptionRecordRepository;

    @Override
    public List<UserAdminResponse> listUsers() {
        return userRepository.findAll().stream()
                .map(this::toAdminResponse)
                .collect(Collectors.toList());
    }

    private UserAdminResponse updateUserRole(User user, UserRole role) {
        user.setRole(role);
        userRepository.save(user);
        return toAdminResponse(user);
    }

    @Override
    public UserAdminResponse updateUserRole(UUID userId, UserRole role) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return updateUserRole(user, role);
    }

    @Override
    public void deleteUser(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if (user.getRole() == UserRole.ADMIN) {
            throw new BadRequestException("Cannot delete administrator accounts");
        }
        userRepository.delete(user);
    }

    @Override
    public ConsumptionAnalyticsResponse getConsumptionAnalytics(Instant from, Instant to) {
        long ticketsServed = consumptionRecordRepository.countServedTicketsBetween(from, to);
        long litersSupplied = consumptionRecordRepository.sumLitersSuppliedBetween(from, to);
        return ConsumptionAnalyticsResponse.builder()
                .from(from)
                .to(to)
                .ticketsServed(ticketsServed)
                .litersSupplied(litersSupplied)
                .build();
    }

    private UserAdminResponse toAdminResponse(User user) {
        return UserAdminResponse.builder()
                .id(user.getId())
                .phone(user.getPhone())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
