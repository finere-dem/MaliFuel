package com.bamako.fuelqueue.service.impl;

import com.bamako.fuelqueue.domain.entity.Locality;
import com.bamako.fuelqueue.domain.entity.Station;
import com.bamako.fuelqueue.domain.enumeration.FuelType;
import com.bamako.fuelqueue.domain.mapper.LocalityMapper;
import com.bamako.fuelqueue.domain.mapper.StationMapper;
import com.bamako.fuelqueue.domain.repository.LocalityRepository;
import com.bamako.fuelqueue.domain.repository.StationRepository;
import com.bamako.fuelqueue.dto.request.LocalityRequest;
import com.bamako.fuelqueue.dto.request.StationCreateRequest;
import com.bamako.fuelqueue.dto.request.StationUpdateRequest;
import com.bamako.fuelqueue.dto.response.LocalityResponse;
import com.bamako.fuelqueue.dto.response.StationResponse;
import com.bamako.fuelqueue.exception.ConflictException;
import com.bamako.fuelqueue.exception.ResourceNotFoundException;
import com.bamako.fuelqueue.service.StationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class StationServiceImpl implements StationService {

    private final LocalityRepository localityRepository;
    private final StationRepository stationRepository;
    private final LocalityMapper localityMapper;
    private final StationMapper stationMapper;

    @Override
    @Transactional(readOnly = true)
    public List<LocalityResponse> getLocalities() {
        return localityRepository.findAll().stream()
            .map(localityMapper::toDto)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public LocalityResponse getLocality(UUID localityId) {
        Locality locality = localityRepository.findById(localityId)
            .orElseThrow(() -> new ResourceNotFoundException("Locality not found."));
        return localityMapper.toDto(locality);
    }

    @Override
    public LocalityResponse createLocality(LocalityRequest request) {
        localityRepository.findByNameIgnoreCase(request.getName())
            .ifPresent(existing -> {
                throw new ConflictException("Locality name already exists.");
            });
        Locality locality = localityMapper.toEntity(request);
        Locality saved = localityRepository.save(locality);
        return localityMapper.toDto(saved);
    }

    @Override
    public LocalityResponse updateLocality(UUID localityId, LocalityRequest request) {
        Locality locality = localityRepository.findById(localityId)
            .orElseThrow(() -> new ResourceNotFoundException("Locality not found."));

        localityRepository.findByNameIgnoreCase(request.getName())
            .filter(existing -> !existing.getId().equals(localityId))
            .ifPresent(existing -> {
                throw new ConflictException("Locality name already exists.");
            });

        localityMapper.updateEntity(request, locality);
        return localityMapper.toDto(locality);
    }

    @Override
    public void deleteLocality(UUID localityId) {
        Locality locality = localityRepository.findById(localityId)
            .orElseThrow(() -> new ResourceNotFoundException("Locality not found."));
        localityRepository.delete(locality);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StationResponse> getStationsByLocality(UUID localityId, FuelType fuelType) {
        Locality locality = localityRepository.findById(localityId)
            .orElseThrow(() -> new ResourceNotFoundException("Locality not found."));
        return stationRepository.findByLocalityId(locality.getId()).stream()
            .filter(station -> fuelType == null || station.getMaxCapacityPerFuel().containsKey(fuelType))
            .map(stationMapper::toDto)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public StationResponse getStation(UUID stationId) {
        Station station = stationRepository.findWithLocalityById(stationId)
            .orElseThrow(() -> new ResourceNotFoundException("Station not found."));
        return stationMapper.toDto(station);
    }

    @Override
    public StationResponse createStation(StationCreateRequest request) {
        stationRepository.findByNameIgnoreCase(request.getName())
            .ifPresent(existing -> {
                throw new ConflictException("Station name already exists.");
            });

        Locality locality = localityRepository.findById(request.getLocalityId())
            .orElseThrow(() -> new ResourceNotFoundException("Locality not found."));

        Station station = stationMapper.toEntity(request);
        station.setLocality(locality);
        Station saved = stationRepository.save(station);
        return stationMapper.toDto(saved);
    }

    @Override
    public StationResponse updateStation(UUID stationId, StationUpdateRequest request) {
        Station station = stationRepository.findById(stationId)
            .orElseThrow(() -> new ResourceNotFoundException("Station not found."));

        if (request.getName() != null) {
            stationRepository.findByNameIgnoreCase(request.getName())
                .filter(existing -> !existing.getId().equals(stationId))
                .ifPresent(existing -> {
                    throw new ConflictException("Station name already exists.");
                });
        }

        if (request.getLocalityId() != null && (station.getLocality() == null ||
            !station.getLocality().getId().equals(request.getLocalityId()))) {
            Locality locality = localityRepository.findById(request.getLocalityId())
                .orElseThrow(() -> new ResourceNotFoundException("Locality not found."));
            station.setLocality(locality);
        }

        stationMapper.updateEntity(request, station);
        return stationMapper.toDto(station);
    }

    @Override
    public void deleteStation(UUID stationId) {
        Station station = stationRepository.findById(stationId)
            .orElseThrow(() -> new ResourceNotFoundException("Station not found."));
        stationRepository.delete(station);
    }
}
