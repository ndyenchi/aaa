package com.example.AOManager.repository;

import com.example.AOManager.entity.ProductImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductImageRepository extends JpaRepository<ProductImageEntity, UUID> {

    Optional<List<ProductImageEntity>> findByProductId_Id(UUID id);
}
