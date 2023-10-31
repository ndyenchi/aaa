package com.example.AOManager.repository;

import com.example.AOManager.entity.ImportDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ImportDetailRepository extends JpaRepository<ImportDetailEntity, UUID> {

    boolean existsByProductId_Id(UUID id);
}
