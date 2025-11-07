package com.bamako.fuelqueue.controller;

import com.bamako.fuelqueue.domain.dto.common.LocalityDto;
import com.bamako.fuelqueue.domain.dto.station.StationResponse;
import com.bamako.fuelqueue.domain.enums.FuelType;
import com.bamako.fuelqueue.service.StationService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class StationController {

    private final StationService stationService;

    @GetMapping("/localities")
    public ResponseEntity<List<LocalityDto>> listLocalities() {
        return ResponseEntity.ok(stationService.getAllLocalities());
    }

    @GetMapping("/localities/{localityId}/stations")
    public ResponseEntity<List<StationResponse>> listStations(
            @PathVariable UUID localityId,
            @RequestParam(name = "fuelType", required = false) FuelType fuelType) {
        return ResponseEntity.ok(stationService.getStationsByLocality(localityId, fuelType));
    }
}
