package com.bamako.fuelqueue.service;

import com.bamako.fuelqueue.domain.dto.common.LocalityDto;
import com.bamako.fuelqueue.domain.dto.station.CreateStationRequest;
import com.bamako.fuelqueue.domain.dto.station.StationResponse;
import com.bamako.fuelqueue.domain.dto.station.UpdateStationRequest;
import com.bamako.fuelqueue.domain.dto.station.UpsertLocalityRequest;
import com.bamako.fuelqueue.domain.enums.FuelType;
import java.util.List;
import java.util.UUID;

public interface StationService {

    List<LocalityDto> getAllLocalities();

    LocalityDto createLocality(UpsertLocalityRequest request);

    LocalityDto updateLocality(UUID localityId, UpsertLocalityRequest request);

    void deleteLocality(UUID localityId);

    List<StationResponse> getStationsByLocality(UUID localityId, FuelType fuelType);

    StationResponse createStation(CreateStationRequest request);

    StationResponse updateStation(UUID stationId, UpdateStationRequest request);

    void deleteStation(UUID stationId);
}
