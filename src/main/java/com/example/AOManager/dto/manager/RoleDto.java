package com.example.AOManager.dto.manager;

import com.example.AOManager.entity.RoleEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleDto implements Serializable {

    private String id;
    private String name;

    public RoleDto(RoleEntity roleEntity) {
        this.id = roleEntity.getId().toString();
        this.name = roleEntity.getName();
    }

    public RoleEntity toEntity() {
        return new RoleEntity(UUID.fromString(this.getId()), this.getName(), null);
    }
}
