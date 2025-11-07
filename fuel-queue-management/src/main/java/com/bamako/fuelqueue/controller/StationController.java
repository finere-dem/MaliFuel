package com.bamako.fuelqueue.controller;

import com.bamako.fuelqueue.dto.station.LocalityResponse;
import com.bamako.fuelqueue.dto.station.StationResponse;
import com.bamako.fuelqueue.enums.FuelType;
import com.bamako.fuelqueue.service.StationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class StationController {

    private final StationService stationService;

    @GetMapping("/localities")
    public ResponseEntity<List<LocalityResponse>> getLocalities() {
        return ResponseEntity.ok(stationService.getAllLocalities());
    }

    @GetMapping("/localities/{id}/stations")
    public ResponseEntity<List<StationResponse>> getStations(
            @PathVariable("id") UUID localityId,
            @RequestParam(value = "fuelType", required = false) FuelType fuelType) {
        return ResponseEntity.ok(stationService.getStationsByLocality(localityId, fuelType));
    }
}
