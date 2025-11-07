package com.bamako.fuelqueue.service;

import com.bamako.fuelqueue.domain.dto.admin.ConsumptionAnalyticsResponse;
import com.bamako.fuelqueue.domain.dto.admin.UserAdminResponse;
import com.bamako.fuelqueue.domain.enums.UserRole;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface AdminService {

    List<UserAdminResponse> listUsers();

    UserAdminResponse updateUserRole(UUID userId, UserRole role);

    void deleteUser(UUID userId);

    ConsumptionAnalyticsResponse getConsumptionAnalytics(Instant from, Instant to);
}
