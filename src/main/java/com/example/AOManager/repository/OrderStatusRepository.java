package com.example.AOManager.repository;

import com.example.AOManager.entity.OrderStatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface OrderStatusRepository extends JpaRepository<OrderStatusEntity, UUID> {

    Optional<OrderStatusEntity> findById(UUID id);
    Optional<OrderStatusEntity> findByName(String name);
}
