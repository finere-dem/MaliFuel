package com.bamako.fuelqueue.controller;

import com.bamako.fuelqueue.domain.dto.admin.ConsumptionAnalyticsResponse;
import com.bamako.fuelqueue.domain.dto.admin.UpdateUserRoleRequest;
import com.bamako.fuelqueue.domain.dto.admin.UserAdminResponse;
import com.bamako.fuelqueue.domain.dto.common.LocalityDto;
import com.bamako.fuelqueue.domain.dto.station.CreateStationRequest;
import com.bamako.fuelqueue.domain.dto.station.StationResponse;
import com.bamako.fuelqueue.domain.dto.station.UpdateStationRequest;
import com.bamako.fuelqueue.domain.dto.station.UpsertLocalityRequest;
import com.bamako.fuelqueue.service.AdminService;
import com.bamako.fuelqueue.service.StationService;
import jakarta.validation.Valid;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
public class AdminController {

    private final StationService stationService;
    private final AdminService adminService;

    @PostMapping("/localities")
    public ResponseEntity<LocalityDto> createLocality(@Valid @RequestBody UpsertLocalityRequest request) {
        LocalityDto response = stationService.createLocality(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/localities/{localityId}")
    public ResponseEntity<LocalityDto> updateLocality(
            @PathVariable UUID localityId,
            @Valid @RequestBody UpsertLocalityRequest request) {
        return ResponseEntity.ok(stationService.updateLocality(localityId, request));
    }

    @DeleteMapping("/localities/{localityId}")
    public ResponseEntity<Void> deleteLocality(@PathVariable UUID localityId) {
        stationService.deleteLocality(localityId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/stations")
    public ResponseEntity<StationResponse> createStation(@Valid @RequestBody CreateStationRequest request) {
        StationResponse response = stationService.createStation(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/stations/{stationId}")
    public ResponseEntity<StationResponse> updateStation(
            @PathVariable UUID stationId,
            @Valid @RequestBody UpdateStationRequest request) {
        return ResponseEntity.ok(stationService.updateStation(stationId, request));
    }

    @DeleteMapping("/stations/{stationId}")
    public ResponseEntity<Void> deleteStation(@PathVariable UUID stationId) {
        stationService.deleteStation(stationId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserAdminResponse>> listUsers() {
        return ResponseEntity.ok(adminService.listUsers());
    }

    @PatchMapping("/users/{userId}/role")
    public ResponseEntity<UserAdminResponse> updateUserRole(
            @PathVariable UUID userId,
            @Valid @RequestBody UpdateUserRoleRequest request) {
        return ResponseEntity.ok(adminService.updateUserRole(userId, request.getRole()));
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID userId) {
        adminService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/analytics/consumption")
    public ResponseEntity<ConsumptionAnalyticsResponse> getConsumptionAnalytics(
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant from,
            @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant to) {
        return ResponseEntity.ok(adminService.getConsumptionAnalytics(from, to));
    }
}
