package com.bamako.fuelqueue.service;

import com.bamako.fuelqueue.dto.station.LocalityRequest;
import com.bamako.fuelqueue.dto.station.LocalityResponse;
import com.bamako.fuelqueue.dto.station.StationCreateRequest;
import com.bamako.fuelqueue.dto.station.StationResponse;
import com.bamako.fuelqueue.dto.station.StationUpdateRequest;
import com.bamako.fuelqueue.entity.Locality;
import com.bamako.fuelqueue.entity.Station;
import com.bamako.fuelqueue.enums.FuelType;
import com.bamako.fuelqueue.exception.BadRequestException;
import com.bamako.fuelqueue.exception.ResourceNotFoundException;
import com.bamako.fuelqueue.mapper.LocalityMapper;
import com.bamako.fuelqueue.mapper.StationMapper;
import com.bamako.fuelqueue.repository.LocalityRepository;
import com.bamako.fuelqueue.repository.StationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StationService {

    private final LocalityRepository localityRepository;
    private final StationRepository stationRepository;
    private final LocalityMapper localityMapper;
    private final StationMapper stationMapper;

    @Transactional(readOnly = true)
    public List<LocalityResponse> getAllLocalities() {
        return localityRepository.findAll().stream()
                .sorted(Comparator.comparing(Locality::getName))
                .map(localityMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<StationResponse> getStationsByLocality(UUID localityId, FuelType fuelType) {
        return stationRepository.findByLocalityId(localityId).stream()
                .filter(station -> fuelType == null || supportsFuelType(station, fuelType))
                .map(stationMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public LocalityResponse createLocality(LocalityRequest request) {
        localityRepository.findByNameIgnoreCaseAndRegionIgnoreCase(request.getName(), request.getRegion())
                .ifPresent(locality -> {
                    throw new BadRequestException("Locality already exists in this region");
                });
        Locality locality = Locality.builder()
                .name(request.getName())
                .region(request.getRegion())
                .build();
        localityRepository.save(locality);
        return localityMapper.toResponse(locality);
    }

    @Transactional
    public LocalityResponse updateLocality(UUID localityId, LocalityRequest request) {
        Locality locality = localityRepository.findById(localityId)
                .orElseThrow(() -> new ResourceNotFoundException("Locality not found"));
        locality.setName(request.getName());
        locality.setRegion(request.getRegion());
        localityRepository.save(locality);
        return localityMapper.toResponse(locality);
    }

    @Transactional
    public void deleteLocality(UUID localityId) {
        if (!localityRepository.existsById(localityId)) {
            throw new ResourceNotFoundException("Locality not found");
        }
        localityRepository.deleteById(localityId);
    }

    @Transactional
    public StationResponse createStation(StationCreateRequest request) {
        Locality locality = localityRepository.findById(request.getLocalityId())
                .orElseThrow(() -> new ResourceNotFoundException("Locality not found"));

        if (stationRepository.existsByNameIgnoreCaseAndLocalityId(request.getName(), locality.getId())) {
            throw new BadRequestException("Station with the same name already exists in this locality");
        }

        Station station = Station.builder()
                .name(request.getName())
                .locality(locality)
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .ravitailed(request.isRavitailed())
                .maxCapacityPerFuel(sanitizeCapacities(request.getMaxCapacityPerFuel()))
                .build();
        stationRepository.save(station);
        return stationMapper.toResponse(station);
    }

    @Transactional
    public StationResponse updateStation(UUID stationId, StationUpdateRequest request) {
        Station station = stationRepository.findById(stationId)
                .orElseThrow(() -> new ResourceNotFoundException("Station not found"));

        if (request.getName() != null && !request.getName().isBlank()) {
            if (!station.getName().equalsIgnoreCase(request.getName())
                    && stationRepository.existsByNameIgnoreCaseAndLocalityId(request.getName(), station.getLocality().getId())) {
                throw new BadRequestException("Station with the same name already exists in this locality");
            }
            station.setName(request.getName());
        }

        if (request.getLatitude() != null) {
            station.setLatitude(request.getLatitude());
        }

        if (request.getLongitude() != null) {
            station.setLongitude(request.getLongitude());
        }

        if (request.getRavitailed() != null) {
            station.setRavitailed(request.getRavitailed());
        }

        if (request.getMaxCapacityPerFuel() != null && !request.getMaxCapacityPerFuel().isEmpty()) {
            station.setMaxCapacityPerFuel(sanitizeCapacities(request.getMaxCapacityPerFuel()));
        }

        stationRepository.save(station);
        return stationMapper.toResponse(station);
    }

    @Transactional
    public void deleteStation(UUID stationId) {
        if (!stationRepository.existsById(stationId)) {
            throw new ResourceNotFoundException("Station not found");
        }
        stationRepository.deleteById(stationId);
    }

    private boolean supportsFuelType(Station station, FuelType fuelType) {
        return station.getMaxCapacityPerFuel() != null && station.getMaxCapacityPerFuel().containsKey(fuelType);
    }

    private Map<FuelType, Integer> sanitizeCapacities(Map<FuelType, Integer> capacities) {
        if (capacities == null || capacities.isEmpty()) {
            throw new BadRequestException("Fuel capacities must be provided");
        }
        Map<FuelType, Integer> sanitized = new EnumMap<>(FuelType.class);
        capacities.forEach((fuelType, capacity) -> {
            if (capacity == null || capacity <= 0) {
                throw new BadRequestException("Fuel capacity must be greater than zero");
            }
            sanitized.put(fuelType, capacity);
        });
        return sanitized;
    }
}
