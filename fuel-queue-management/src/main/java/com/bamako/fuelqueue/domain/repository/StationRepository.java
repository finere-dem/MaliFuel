package com.bamako.fuelqueue.domain.repository;

import com.bamako.fuelqueue.domain.entity.Station;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StationRepository extends JpaRepository<Station, UUID> {
    @EntityGraph(attributePaths = {"locality"})
    List<Station> findByLocalityId(UUID localityId);
    Optional<Station> findByNameIgnoreCase(String name);

    @EntityGraph(attributePaths = {"locality"})
    Optional<Station> findWithLocalityById(UUID id);
}
