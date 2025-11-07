package com.bamako.fuelqueue.repository;

import com.bamako.fuelqueue.entity.Station;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StationRepository extends JpaRepository<Station, UUID> {

    Optional<Station> findByNameIgnoreCaseAndLocalityId(String name, UUID localityId);

    boolean existsByNameIgnoreCaseAndLocalityId(String name, UUID localityId);

    List<Station> findByLocalityId(UUID localityId);
}
