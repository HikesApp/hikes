package com.chekalin.hikes.repositories;

import com.chekalin.hikes.domain.Hike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface HikeRepository extends JpaRepository<Hike, UUID> {

    Optional<Hike> findById(UUID id);
}
