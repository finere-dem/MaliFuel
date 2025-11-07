package com.bamako.fuelqueue.controller;

import com.bamako.fuelqueue.dto.request.AdminUpdateUserRoleRequest;
import com.bamako.fuelqueue.dto.request.LocalityRequest;
import com.bamako.fuelqueue.dto.request.StationCreateRequest;
import com.bamako.fuelqueue.dto.request.StationUpdateRequest;
import com.bamako.fuelqueue.dto.response.AnalyticsResponse;
import com.bamako.fuelqueue.dto.response.LocalityResponse;
import com.bamako.fuelqueue.dto.response.StationResponse;
import com.bamako.fuelqueue.dto.response.UserResponse;
import com.bamako.fuelqueue.service.AdminService;
import com.bamako.fuelqueue.service.AnalyticsService;
import com.bamako.fuelqueue.service.StationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminService adminService;
    private final StationService stationService;
    private final AnalyticsService analyticsService;

    @GetMapping("/users")
    public ResponseEntity<List<UserResponse>> listUsers() {
        return ResponseEntity.ok(adminService.listUsers());
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<UserResponse> getUser(@PathVariable UUID userId) {
        return ResponseEntity.ok(adminService.getUser(userId));
    }

    @PutMapping("/users/{userId}/role")
    public ResponseEntity<UserResponse> updateUserRole(
        @PathVariable UUID userId,
        @Valid @RequestBody AdminUpdateUserRoleRequest request) {
        return ResponseEntity.ok(adminService.updateUserRole(userId, request.getRole()));
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID userId) {
        adminService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/localities")
    public ResponseEntity<LocalityResponse> createLocality(@Valid @RequestBody LocalityRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(stationService.createLocality(request));
    }

    @PutMapping("/localities/{localityId}")
    public ResponseEntity<LocalityResponse> updateLocality(
        @PathVariable UUID localityId,
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
        return ResponseEntity.status(HttpStatus.CREATED).body(stationService.createStation(request));
    }

    @PutMapping("/stations/{stationId}")
    public ResponseEntity<StationResponse> updateStation(
        @PathVariable UUID stationId,
        @Valid @RequestBody StationUpdateRequest request) {
        return ResponseEntity.ok(stationService.updateStation(stationId, request));
    }

    @DeleteMapping("/stations/{stationId}")
    public ResponseEntity<Void> deleteStation(@PathVariable UUID stationId) {
        stationService.deleteStation(stationId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/stations/{stationId}")
    public ResponseEntity<StationResponse> getStation(@PathVariable UUID stationId) {
        return ResponseEntity.ok(stationService.getStation(stationId));
    }

    @GetMapping("/analytics/consumption")
    public ResponseEntity<AnalyticsResponse> getAnalytics(
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime from,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime to) {
        return ResponseEntity.ok(analyticsService.getConsumptionAnalytics(from, to));
    }
}
