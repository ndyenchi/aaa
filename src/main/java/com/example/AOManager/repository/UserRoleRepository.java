package com.example.AOManager.repository;

import com.example.AOManager.entity.UserRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRoleRepository extends JpaRepository<UserRoleEntity, UUID> {
    Optional<List<UserRoleEntity>> findByRoleId_Id(UUID id);

    Optional<UserRoleEntity> findByUserId_Id(UUID id);

    @Query(value = "SELECT ur.* \n" +
            "FROM user_role ur \n" +
            "INNER JOIN role r ON ur.role_id = r.id \n" +
            "INNER JOIN users u ON ur.user_id = u.id \n" +
            "WHERE (:roleId IS NULL OR r.id = :roleId) AND (1<>1 \n" +
            "OR CAST(u.first_name AS text) ILIKE CONCAT('%', :keyWord, '%') \n" +
            "OR CAST(u.last_name AS text) ILIKE CONCAT('%', :keyWord, '%') \n" +
            "OR CAST(u.email AS text) ILIKE CONCAT('%', :keyWord, '%') \n" +
            "OR CAST(u.address AS text) ILIKE CONCAT('%', :keyWord, '%'))", nativeQuery = true)
    Optional<List<UserRoleEntity>> getCountRecord(UUID roleId, String keyWord);
}
