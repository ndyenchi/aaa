package com.example.AOManager.repository;

import com.example.AOManager.entity.ImportFormEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ImportFormRepository extends JpaRepository<ImportFormEntity, UUID> {

    @Query(value = "SELECT i.* FROM import_form i \n" +
            "INNER JOIN users u ON i.employee_id = u.id \n" +
            "INNER JOIN order_supplier os ON i.order_supplier_id = os.id \n" +
            "WHERE CAST(i.id AS text) ILIKE CONCAT('%', :keyWord, '%') \n" +
            "OR CAST(os.id AS text) ILIKE CONCAT('%', :keyWord, '%') \n" +
            "OR CAST(os.supplier_name AS text) ILIKE CONCAT('%', :keyWord, '%') \n" +
            "OR CAST(i.create_date AS text) ILIKE CONCAT('%', :keyWord, '%') \n" +
            "OR CAST(u.first_name AS text) ILIKE CONCAT('%', :keyWord, '%') \n" +
            "OR CAST(u.last_name AS text) ILIKE CONCAT('%', :keyWord, '%')",nativeQuery = true)
    Optional<List<ImportFormEntity>> getCountRecord(String keyWord);

    @Query(value = "SELECT i.* FROM import_form i \n" +
            "INNER JOIN users u ON i.employee_id = u.id \n" +
            "INNER JOIN order_supplier os ON i.order_supplier_id = os.id \n" +
            "WHERE CAST(i.id AS text) ILIKE CONCAT('%', :keyWord, '%') \n" +
            "OR CAST(os.id AS text) ILIKE CONCAT('%', :keyWord, '%') \n" +
            "OR CAST(os.supplier_name AS text) ILIKE CONCAT('%', :keyWord, '%') \n" +
            "OR CAST(i.create_date AS text) ILIKE CONCAT('%', :keyWord, '%') \n" +
            "OR CAST(u.first_name AS text) ILIKE CONCAT('%', :keyWord, '%') \n" +
            "OR CAST(u.last_name AS text) ILIKE CONCAT('%', :keyWord, '%') \n" +
            "ORDER BY i.updated_at DESC, i.created_at DESC " +
            "LIMIT :limit OFFSET :page", nativeQuery = true)
    Optional<List<ImportFormEntity>> getImportFormList(int page, int limit, String keyWord);
}
