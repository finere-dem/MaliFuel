package com.bamako.fuelqueue.repository;

import com.bamako.fuelqueue.domain.entity.User;
import com.bamako.fuelqueue.domain.entity.Vehicle;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleRepository extends JpaRepository<Vehicle, UUID> {

    Optional<Vehicle> findByUser(User user);

    boolean existsByPlateNumber(String plateNumber);
}
