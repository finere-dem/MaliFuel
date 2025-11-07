package com.bamako.fuelqueue.domain.repository;

import com.bamako.fuelqueue.domain.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface VehicleRepository extends JpaRepository<Vehicle, UUID> {
    Optional<Vehicle> findByUserId(UUID userId);
    boolean existsByPlateNumber(String plateNumber);
}
