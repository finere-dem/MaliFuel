package com.bamako.fuelqueue.repository;

import com.bamako.fuelqueue.domain.entity.Locality;
import com.bamako.fuelqueue.domain.entity.User;
import com.bamako.fuelqueue.domain.entity.UserLocality;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserLocalityRepository extends JpaRepository<UserLocality, UUID> {

    List<UserLocality> findByUser(User user);

    Optional<UserLocality> findByUserAndIsPrimary(User user, boolean isPrimary);

    boolean existsByUserAndLocality(User user, Locality locality);
}
