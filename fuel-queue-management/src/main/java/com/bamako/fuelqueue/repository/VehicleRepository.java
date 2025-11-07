package com.bamako.fuelqueue.repository;

import com.bamako.fuelqueue.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface VehicleRepository extends JpaRepository<Vehicle, UUID> {

    Optional<Vehicle> findByPlateNumber(String plateNumber);

    boolean existsByPlateNumber(String plateNumber);

    Optional<Vehicle> findByUserId(UUID userId);
}
