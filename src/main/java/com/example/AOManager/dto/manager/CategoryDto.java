package com.example.AOManager.dto.manager;

import com.example.AOManager.common.Function;
import com.example.AOManager.entity.CategoryEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {

    private String id;
    private String name;
    private long createAt;
    private long updateAt;

    public CategoryDto(CategoryEntity categoryEntity) {
        this.id = categoryEntity.getId().toString();
        this.name = categoryEntity.getName();
        this.setCreateAt(null != categoryEntity.getCreatedAt() ? Function.toLongFromTimeStamp(categoryEntity.getCreatedAt()) : 0);
        this.setUpdateAt(null != categoryEntity.getUpdatedAt() ? Function.toLongFromTimeStamp(categoryEntity.getUpdatedAt()) : 0);
    }
}
