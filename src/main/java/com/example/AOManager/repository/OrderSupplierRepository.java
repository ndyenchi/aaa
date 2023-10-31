package com.example.AOManager.repository;

import com.example.AOManager.entity.OrderSupplierEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderSupplierRepository extends JpaRepository<OrderSupplierEntity, UUID> {

    Optional<OrderSupplierEntity> findById(UUID id);

    Optional<List<OrderSupplierEntity>> findByStatus(String status);

    @Query(value = "SELECT o.* FROM order_supplier o \n" +
            "INNER JOIN users u ON o.employee_id = u.id \n" +
            "WHERE o.status = :status AND (1<>1 \n" +
            "OR CAST(o.id AS text) ILIKE CONCAT('%', :keyWord, '%') \n" +
            "OR CAST(o.supplier_name AS text) ILIKE CONCAT('%', :keyWord, '%') \n" +
            "OR CAST(o.order_date AS text) ILIKE CONCAT('%', :keyWord, '%') \n" +
            "OR CAST(o.delivery_date AS text) ILIKE CONCAT('%', :keyWord, '%') \n" +
            "OR CAST(u.first_name AS text) ILIKE CONCAT('%', :keyWord, '%') \n" +
            "OR CAST(u.last_name AS text) ILIKE CONCAT('%', :keyWord, '%'))", nativeQuery = true)
    Optional<List<OrderSupplierEntity>> getCountRecord(String status, String keyWord);

    @Query(value = "SELECT o.* FROM order_supplier o \n" +
            "INNER JOIN users u ON o.employee_id = u.id \n" +
            "WHERE o.status = :status AND (1<>1 \n" +
            "OR CAST(o.id AS text) ILIKE CONCAT('%', :keyWord, '%') \n" +
            "OR CAST(o.supplier_name AS text) ILIKE CONCAT('%', :keyWord, '%') \n" +
            "OR CAST(o.order_date AS text) ILIKE CONCAT('%', :keyWord, '%') \n" +
            "OR CAST(o.delivery_date AS text) ILIKE CONCAT('%', :keyWord, '%') \n" +
            "OR CAST(u.first_name AS text) ILIKE CONCAT('%', :keyWord, '%') \n" +
            "OR CAST(u.last_name AS text) ILIKE CONCAT('%', :keyWord, '%')) \n" +
            "ORDER BY o.updated_at DESC, o.created_at DESC " +
            "LIMIT :limit OFFSET :page", nativeQuery = true)
    Optional<List<OrderSupplierEntity>> getOrderSupplierList(String status, int page, int limit, String keyWord);
}
