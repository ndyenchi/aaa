package com.example.AOManager.repository;

import com.example.AOManager.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<ProductEntity, UUID> {

    Optional<ProductEntity> findById(UUID id);

    Optional<List<ProductEntity>> findByCategoryId_Id(UUID id);

    boolean existsByName(String name);

    @Query(value = "SELECT p.* \n" +
            "FROM product p \n" +
            "INNER JOIN category c ON p.category_id = c.id \n" +
            "WHERE (:categoryId IS NULL OR c.id = :categoryId) AND (1<>1 \n" +
            "OR CAST(p.description AS text) ILIKE CONCAT('%', :keyWord, '%') \n" +
            "OR CAST(p.name AS text) ILIKE CONCAT('%', :keyWord, '%'))", nativeQuery = true)
    Optional<List<ProductEntity>> getCountRecordWithCategory(UUID categoryId, String keyWord);

    @Query(value = "SELECT p.* \n" +
            "FROM product p \n" +
            "INNER JOIN category c ON p.category_id = c.id \n" +
            "WHERE (:categoryId IS NULL OR c.id = :categoryId) AND (1<>1 \n" +
            "OR CAST(p.description AS text) ILIKE CONCAT('%', :keyWord, '%') \n" +
            "OR CAST(p.name AS text) ILIKE CONCAT('%', :keyWord, '%')) \n" +
            "ORDER BY p.updated_at DESC, p.created_at DESC \n" +
            "LIMIT :limit\n" +
            "OFFSET :page", nativeQuery = true)
    Optional<List<ProductEntity>> getProductsListWithCategory(UUID categoryId, int page, int limit, String keyWord);

    @Query(value = "SELECT p.* \n" +
            "FROM product p \n" +
            "INNER JOIN category c ON p.category_id = c.id \n" +
            "WHERE CAST(p.description AS text) ILIKE CONCAT('%', :keyWord, '%') \n" +
            "OR CAST(p.name AS text) ILIKE CONCAT('%', :keyWord, '%')", nativeQuery = true)
    Optional<List<ProductEntity>> getCountRecord(String keyWord);

    @Query(value = "SELECT p.* \n" +
            "FROM product p \n" +
            "INNER JOIN category c ON p.category_id = c.id \n" +
            "WHERE CAST(p.description AS text) ILIKE CONCAT('%', :keyWord, '%') \n" +
            "OR CAST(p.name AS text) ILIKE CONCAT('%', :keyWord, '%') \n" +
            "ORDER BY p.updated_at DESC, p.created_at DESC \n" +
            "LIMIT :limit\n" +
            "OFFSET :page", nativeQuery = true)
    Optional<List<ProductEntity>> getProductsList(int page, int limit, String keyWord);

    @Query(value = "SELECT p.* " +
            "FROM product p " +
            "INNER JOIN ( " +
                "SELECT product_id, MAX(created_at) AS max_created_at " +
                "FROM import_detail " +
                "GROUP BY product_id " +
            ") AS latest_import ON p.id = latest_import.product_id " +
            "WHERE p.status = true " +
            "ORDER BY latest_import.max_created_at DESC;", nativeQuery = true)
    Optional<List<ProductEntity>> getNewProductsList();

    @Query(value = "SELECT * " +
            "FROM product " +
            "WHERE status = true " +
            "ORDER BY sold_quantity DESC " +
            "LIMIT 10 ", nativeQuery = true)
    Optional<List<ProductEntity>> getBestSellingProductsList();

    @Query(value = "SELECT p.* " +
            "FROM product p " +
            "INNER JOIN category c ON p.category_id = c.id " +
            "WHERE (:categoryId IS NULL OR (c.id = :categoryId)) AND p.status = true AND (1<>1 " +
            "OR CAST(p.description AS text) ILIKE CONCAT('%', :keyWord, '%') " +
            "OR CAST(p.name AS text) ILIKE CONCAT('%', :keyWord, '%')) " +
            "ORDER BY p.sold_quantity DESC ", nativeQuery = true)
    Optional<List<ProductEntity>> getProductsListForCustomerWithCategory(UUID categoryId, String keyWord);

    @Query(value = "SELECT p.* " +
            "FROM product p " +
            "INNER JOIN category c ON p.category_id = c.id " +
            "WHERE p.status = true AND (1<>1 " +
            "OR CAST(p.description AS text) ILIKE CONCAT('%', :keyWord, '%') " +
            "OR CAST(p.name AS text) ILIKE CONCAT('%', :keyWord, '%')) " +
            "ORDER BY p.sold_quantity DESC ", nativeQuery = true)
    Optional<List<ProductEntity>> getProductsListForCustomerWithoutCategory(String keyWord);
}
