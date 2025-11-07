package com.bamako.fuelqueue.controller;

import com.bamako.fuelqueue.dto.analytics.ConsumptionAnalyticsResponse;
import com.bamako.fuelqueue.dto.station.LocalityRequest;
import com.bamako.fuelqueue.dto.station.LocalityResponse;
import com.bamako.fuelqueue.dto.station.StationCreateRequest;
import com.bamako.fuelqueue.dto.station.StationResponse;
import com.bamako.fuelqueue.dto.station.StationUpdateRequest;
import com.bamako.fuelqueue.dto.user.UpdateUserRoleRequest;
import com.bamako.fuelqueue.dto.user.UserResponse;
import com.bamako.fuelqueue.service.AdminService;
import com.bamako.fuelqueue.service.AnalyticsService;
import com.bamako.fuelqueue.service.StationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final StationService stationService;
    private final AnalyticsService analyticsService;

    @GetMapping("/users")
    public ResponseEntity<List<UserResponse>> getUsers() {
        return ResponseEntity.ok(adminService.getAllUsers());
    }

    @PutMapping("/users/{userId}/role")
    public ResponseEntity<UserResponse> updateUserRole(@PathVariable UUID userId,
                                                       @Valid @RequestBody UpdateUserRoleRequest request) {
        return ResponseEntity.ok(adminService.updateUserRole(userId, request));
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID userId) {
        adminService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/localities")
    public ResponseEntity<LocalityResponse> createLocality(@Valid @RequestBody LocalityRequest request) {
        return ResponseEntity.ok(stationService.createLocality(request));
    }

    @PutMapping("/localities/{localityId}")
    public ResponseEntity<LocalityResponse> updateLocality(@PathVariable UUID localityId,
                                                           @Valid @RequestBody LocalityRequest request) {
        return ResponseEntity.ok(stationService.updateLocality(localityId, request));
    }

    @DeleteMapping("/localities/{localityId}")
    public ResponseEntity<Void> deleteLocality(@PathVariable UUID localityId) {
        stationService.deleteLocality(localityId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/stations")
    public ResponseEntity<StationResponse> createStation(@Valid @RequestBody StationCreateRequest request) {
        return ResponseEntity.ok(stationService.createStation(request));
    }

    @PutMapping("/stations/{stationId}")
    public ResponseEntity<StationResponse> updateStation(@PathVariable UUID stationId,
                                                         @Valid @RequestBody StationUpdateRequest request) {
        return ResponseEntity.ok(stationService.updateStation(stationId, request));
    }

    @DeleteMapping("/stations/{stationId}")
    public ResponseEntity<Void> deleteStation(@PathVariable UUID stationId) {
        stationService.deleteStation(stationId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/analytics/consumption")
    public ResponseEntity<ConsumptionAnalyticsResponse> getConsumptionAnalytics(
            @RequestParam(value = "from", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant from,
            @RequestParam(value = "to", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant to) {
        return ResponseEntity.ok(analyticsService.getConsumptionAnalytics(from, to));
    }
}
