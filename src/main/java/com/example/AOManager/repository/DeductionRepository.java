package com.example.AOManager.repository;

import com.example.AOManager.entity.DeductionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DeductionRepository extends JpaRepository<DeductionEntity, UUID> {

    boolean existsByProductId_Id(UUID id);
}
