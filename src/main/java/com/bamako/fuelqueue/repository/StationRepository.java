package com.bamako.fuelqueue.repository;

import com.bamako.fuelqueue.domain.entity.Locality;
import com.bamako.fuelqueue.domain.entity.Station;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StationRepository extends JpaRepository<Station, UUID> {

    boolean existsByNameIgnoreCase(String name);

    Optional<Station> findByNameIgnoreCase(String name);

    List<Station> findByLocality(Locality locality);

    List<Station> findByLocalityId(UUID localityId);
}
