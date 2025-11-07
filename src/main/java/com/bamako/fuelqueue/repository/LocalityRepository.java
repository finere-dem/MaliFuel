package com.bamako.fuelqueue.repository;

import com.bamako.fuelqueue.domain.entity.Locality;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocalityRepository extends JpaRepository<Locality, UUID> {

    boolean existsByNameIgnoreCase(String name);

    Optional<Locality> findByNameIgnoreCase(String name);
}
