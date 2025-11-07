package com.bamako.fuelqueue.domain.repository;

import com.bamako.fuelqueue.domain.entity.UserLocality;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserLocalityRepository extends JpaRepository<UserLocality, UUID> {
    List<UserLocality> findByUserId(UUID userId);
    Optional<UserLocality> findByUserIdAndPrimaryLocalityTrue(UUID userId);
    boolean existsByUserIdAndPrimaryLocalityTrue(UUID userId);
}
