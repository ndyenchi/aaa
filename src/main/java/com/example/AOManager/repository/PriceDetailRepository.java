package com.example.AOManager.repository;

import com.example.AOManager.entity.PriceDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PriceDetailRepository extends JpaRepository<PriceDetailEntity, UUID> {

    boolean existsByProductId_Id(UUID id);

    @Query(value = "SELECT pd.* " +
            "FROM price_detail pd " +
            "LEFT JOIN product p ON pd.product_id = p.id " +
            "WHERE pd.product_id = :productId " +
            "ORDER BY pd.updated_at DESC, pd.created_at DESC", nativeQuery = true)
    Optional<List<PriceDetailEntity>> getPriceDetailsListWithProductId(UUID productId);
}
