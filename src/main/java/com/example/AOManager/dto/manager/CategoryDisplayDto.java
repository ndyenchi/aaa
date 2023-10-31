package com.example.AOManager.dto.manager;

import com.example.AOManager.entity.CategoryEntity;
import com.example.AOManager.repository.CategoryRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDisplayDto {

    private String id;
    private String name;
    private int productCount;

    public CategoryDisplayDto(CategoryEntity categoryEntity) {
        this.id = categoryEntity.getId().toString();
        this.name = categoryEntity.getName();
        this.productCount = categoryEntity.getProductList().size();
    }
}
