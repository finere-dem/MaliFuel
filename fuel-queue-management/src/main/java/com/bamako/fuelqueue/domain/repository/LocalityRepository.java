package com.bamako.fuelqueue.domain.repository;

import com.bamako.fuelqueue.domain.entity.Locality;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface LocalityRepository extends JpaRepository<Locality, UUID> {
    Optional<Locality> findByNameIgnoreCase(String name);
    boolean existsByNameIgnoreCase(String name);
}
