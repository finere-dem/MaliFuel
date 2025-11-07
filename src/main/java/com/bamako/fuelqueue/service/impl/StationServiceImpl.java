package com.bamako.fuelqueue.service.impl;

import com.bamako.fuelqueue.domain.dto.common.LocalityDto;
import com.bamako.fuelqueue.domain.dto.station.CreateStationRequest;
import com.bamako.fuelqueue.domain.dto.station.StationResponse;
import com.bamako.fuelqueue.domain.dto.station.UpdateStationRequest;
import com.bamako.fuelqueue.domain.dto.station.UpsertLocalityRequest;
import com.bamako.fuelqueue.domain.entity.Locality;
import com.bamako.fuelqueue.domain.entity.Station;
import com.bamako.fuelqueue.domain.enums.FuelType;
import com.bamako.fuelqueue.domain.mapper.LocalityMapper;
import com.bamako.fuelqueue.domain.mapper.StationMapper;
import com.bamako.fuelqueue.exception.BadRequestException;
import com.bamako.fuelqueue.exception.ResourceNotFoundException;
import com.bamako.fuelqueue.repository.LocalityRepository;
import com.bamako.fuelqueue.repository.StationRepository;
import com.bamako.fuelqueue.service.StationService;
import jakarta.transaction.Transactional;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class StationServiceImpl implements StationService {

    private final LocalityRepository localityRepository;
    private final StationRepository stationRepository;

    @Override
    public List<LocalityDto> getAllLocalities() {
        return localityRepository.findAll().stream()
                .map(LocalityMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public LocalityDto createLocality(UpsertLocalityRequest request) {
        if (localityRepository.existsByNameIgnoreCase(request.getName())) {
            throw new BadRequestException("Locality already exists");
        }
        Locality locality = Locality.builder()
                .name(request.getName())
                .region(request.getRegion())
                .build();
        localityRepository.save(locality);
        return LocalityMapper.toDto(locality);
    }

    @Override
    public LocalityDto updateLocality(UUID localityId, UpsertLocalityRequest request) {
        Locality locality = localityRepository.findById(localityId)
                .orElseThrow(() -> new ResourceNotFoundException("Locality not found"));
        locality.setName(request.getName());
        locality.setRegion(request.getRegion());
        localityRepository.save(locality);
        return LocalityMapper.toDto(locality);
    }

    @Override
    public void deleteLocality(UUID localityId) {
        Locality locality = localityRepository.findById(localityId)
                .orElseThrow(() -> new ResourceNotFoundException("Locality not found"));
        if (!locality.getStations().isEmpty()) {
            throw new BadRequestException("Cannot delete locality with assigned stations");
        }
        localityRepository.delete(locality);
    }

    @Override
    public List<StationResponse> getStationsByLocality(UUID localityId, FuelType fuelType) {
        Locality locality = localityRepository.findById(localityId)
                .orElseThrow(() -> new ResourceNotFoundException("Locality not found"));
        return stationRepository.findByLocality(locality).stream()
                .filter(station -> filterByFuelType(station, fuelType))
                .map(StationMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public StationResponse createStation(CreateStationRequest request) {
        if (stationRepository.existsByNameIgnoreCase(request.getName())) {
            throw new BadRequestException("Station name already exists");
        }
        Locality locality = localityRepository.findById(request.getLocalityId())
                .orElseThrow(() -> new ResourceNotFoundException("Locality not found"));
        Station station = Station.builder()
                .name(request.getName())
                .locality(locality)
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .ravitailed(request.isRavitailed())
                .maxCapacityPerFuel(new EnumMap<>(request.getMaxCapacityPerFuel()))
                .build();
        stationRepository.save(station);
        return StationMapper.toDto(station);
    }

    @Override
    public StationResponse updateStation(UUID stationId, UpdateStationRequest request) {
        Station station = stationRepository.findById(stationId)
                .orElseThrow(() -> new ResourceNotFoundException("Station not found"));
        Locality locality = localityRepository.findById(request.getLocalityId())
                .orElseThrow(() -> new ResourceNotFoundException("Locality not found"));
        station.setName(request.getName());
        station.setLocality(locality);
        station.setLatitude(request.getLatitude());
        station.setLongitude(request.getLongitude());
        station.setRavitailed(request.isRavitailed());
        station.setMaxCapacityPerFuel(new EnumMap<>(request.getMaxCapacityPerFuel()));
        stationRepository.save(station);
        return StationMapper.toDto(station);
    }

    @Override
    public void deleteStation(UUID stationId) {
        Station station = stationRepository.findById(stationId)
                .orElseThrow(() -> new ResourceNotFoundException("Station not found"));
        stationRepository.delete(station);
    }

    private boolean filterByFuelType(Station station, FuelType fuelType) {
        if (fuelType == null) {
            return true;
        }
        Map<FuelType, Integer> capacities = station.getMaxCapacityPerFuel();
        return capacities != null && capacities.containsKey(fuelType) && capacities.get(fuelType) > 0;
    }
}
