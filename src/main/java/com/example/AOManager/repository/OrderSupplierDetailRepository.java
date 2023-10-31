package com.example.AOManager.repository;

import com.example.AOManager.entity.OrderSupplierDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderSupplierDetailRepository extends JpaRepository<OrderSupplierDetailEntity, UUID> {

    boolean existsByProductId_Id(UUID id);
}
