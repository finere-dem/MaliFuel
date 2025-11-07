package com.bamako.fuelqueue.repository;

import com.bamako.fuelqueue.entity.UserLocality;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserLocalityRepository extends JpaRepository<UserLocality, UUID> {

    List<UserLocality> findByUserId(UUID userId);

    long countByUserId(UUID userId);

    Optional<UserLocality> findByUserIdAndPrimaryTrue(UUID userId);
}
