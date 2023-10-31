package com.example.AOManager.repository;

import com.example.AOManager.entity.CartDetailEntity;
import com.example.AOManager.entity.ProductEntity;
import com.example.AOManager.entity.UsersEntity;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CartDetailRepository extends JpaRepository<CartDetailEntity, UUID> {

    boolean existsByProductId_Id(UUID id);
    Optional<List<CartDetailEntity>> findByCustomerId_Id(UUID id);
    @Query(value = "SELECT cd.* " +
            "FROM cart_detail cd " +
            "JOIN users u ON cd.customer_id = u.id " +
            "JOIN product p ON cd.product_id = p.id " +
            "WHERE order_customer_id IS NULL " +
            "AND u.id = :customerId " +
            "AND p.id = :productId", nativeQuery = true)
    Optional<List<CartDetailEntity>> getCartDetailsListToAddToCart(UUID customerId, UUID productId);

    @Query(value = "SELECT cd.* " +
            "FROM cart_detail cd " +
            "JOIN users u ON cd.customer_id = u.id " +
            "JOIN product p ON cd.product_id = p.id " +
            "WHERE order_customer_id IS NULL " +
            "AND u.id = :customerId", nativeQuery = true)
    Optional<List<CartDetailEntity>> getCartDetailsListToOrder(UUID customerId);
}
