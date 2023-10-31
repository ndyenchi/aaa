package com.example.AOManager.repository;

import com.example.AOManager.entity.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UsersRepository extends JpaRepository<UsersEntity, UUID> {

    Optional<UsersEntity> findByEmail(String email);

    boolean existsByEmail(String email);

    Optional<UsersEntity> findByToken(String token);

    @Query(value = "SELECT u.* \n" +
                    "FROM users u\n" +
                    "INNER JOIN user_role ur ON u.id = ur.user_id\n" +
                    "INNER JOIN role r ON ur.role_id = r.id\n" +
                    "WHERE r.id = :roleId AND (1<>1 \n" +
                    "OR CAST(u.first_name AS text) ILIKE CONCAT('%', :keyWord, '%') \n" +
                    "OR CAST(u.last_name AS text) ILIKE CONCAT('%', :keyWord, '%') \n" +
                    "OR CAST(u.email AS text) ILIKE CONCAT('%', :keyWord, '%') \n" +
                    "OR CAST(u.address AS text) ILIKE CONCAT('%', :keyWord, '%')) \n" +
                    "ORDER BY u.updated_at DESC, u.created_at DESC \n" +
                    "LIMIT :limit\n" +
                    "OFFSET :page", nativeQuery = true)
    List<UsersEntity> getUsersList(UUID roleId, int page, int limit, String keyWord);
}
