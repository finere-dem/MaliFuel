package com.bamako.fuelqueue.repository;

import com.bamako.fuelqueue.entity.Locality;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LocalityRepository extends JpaRepository<Locality, UUID> {

    Optional<Locality> findByNameIgnoreCaseAndRegionIgnoreCase(String name, String region);

    List<Locality> findByRegionIgnoreCase(String region);
}
