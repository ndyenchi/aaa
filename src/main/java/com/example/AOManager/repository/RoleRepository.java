package com.example.AOManager.repository;

import com.example.AOManager.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RoleRepository extends JpaRepository<RoleEntity, UUID> {

    Optional<RoleEntity> findById(UUID id);

    Optional<RoleEntity> findByName(String name);

}
