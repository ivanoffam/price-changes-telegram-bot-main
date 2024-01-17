package org.example.repository;

import org.example.entity.PriceSnapshotEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PriceSnapshotRepository extends JpaRepository<PriceSnapshotEntity, Long> {
    List<PriceSnapshotEntity> findAllByUserId(long userId);
}

