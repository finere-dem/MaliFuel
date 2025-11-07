package com.bamako.fuelqueue.service;

import com.bamako.fuelqueue.domain.enumeration.FuelType;
import com.bamako.fuelqueue.dto.request.LocalityRequest;
import com.bamako.fuelqueue.dto.request.StationCreateRequest;
import com.bamako.fuelqueue.dto.request.StationUpdateRequest;
import com.bamako.fuelqueue.dto.response.LocalityResponse;
import com.bamako.fuelqueue.dto.response.StationResponse;

import java.util.List;
import java.util.UUID;

public interface StationService {

    List<LocalityResponse> getLocalities();

    LocalityResponse getLocality(UUID localityId);

    LocalityResponse createLocality(LocalityRequest request);

    LocalityResponse updateLocality(UUID localityId, LocalityRequest request);

    void deleteLocality(UUID localityId);

    List<StationResponse> getStationsByLocality(UUID localityId, FuelType fuelType);

    StationResponse getStation(UUID stationId);

    StationResponse createStation(StationCreateRequest request);

    StationResponse updateStation(UUID stationId, StationUpdateRequest request);

    void deleteStation(UUID stationId);
}
